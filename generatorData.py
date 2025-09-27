import tensorflow as tf
from tokenizers import Tokenizer, models, pre_tokenizers, decoders, trainers, processors

# Étape 1 : Prétraitement des données
tokenizer = Tokenizer(models.BPE())
tokenizer.fit_on_texts(['1F,2D,H;'])
inputs = tokenizer.encode('1F,2D,H;')
outputs = ['F1:L1:D1,D2;', 'F1:C1', 'F1:C2', 'C1:P1:10,80,1,1', 'SR:C1:{1,2,3};']

# Étape 2 : Création du modèle
class Transformer(tf.keras.Model):
    def __init__(self, num_layers, d_model, num_heads, dff, input_vocab_size, target_vocab_size, rate=0.1):
        super(Transformer, self).__init__()

        self.encoder = Encoder(num_layers, d_model, num_heads, dff, input_vocab_size, rate)
        self.decoder = Decoder(num_layers, d_model, num_heads, dff, target_vocab_size, rate)
        self.linear = tf.keras.layers.Dense(target_vocab_size)

    def call(self, inputs, training):
        enc_output = self.encoder(inputs, training)
        dec_output = self.decoder(inputs, enc_output, training)
        return self.linear(dec_output)

class Encoder(tf.keras.layers.Layer):
    def __init__(self, num_layers, d_model, num_heads, dff, input_vocab_size, rate=0.1):
        super(Encoder, self).__init__()

        self.embedding = tf.keras.layers.Embedding(input_vocab_size, d_model)
        self.pos_encoding = PositionalEncoding(d_model)
        self.enc_layers = [EncoderLayer(d_model, num_heads, dff, rate) for _ in range(num_layers)]

    def call(self, x, training):
        x = self.embedding(x)
        x *= tf.math.sqrt(tf.cast(d_model, tf.float32))
        x = self.pos_encoding(x)

        for i in range(len(self.enc_layers)):
            x = self.enc_layers[i](x, training)

        return x

class Decoder(tf.keras.layers.Layer):
    def __init__(self, num_layers, d_model, num_heads, dff, target_vocab_size, rate=0.1):
        super(Decoder, self).__init__()

        self.embedding = tf.keras.layers.Embedding(target_vocab_size, d_model)
        self.pos_encoding = PositionalEncoding(d_model)
        self.dec_layers = [DecoderLayer(d_model, num_heads, dff, rate) for _ in range(num_layers)]

    def call(self, x, enc_output, training):
        x = self.embedding(x)
        x *= tf.math.sqrt(tf.cast(d_model, tf.float32))
        x = self.pos_encoding(x)

        for i in range(len(self.dec_layers)):
            x = self.dec_layers[i](x, enc_output, training)

        return x

class EncoderLayer(tf.keras.layers.Layer):
    def __init__(self, d_model, num_heads, dff, rate=0.1):
        super(EncoderLayer, self).__init__()

        self.mha = MultiHeadAttention(d_model, num_heads)
        self.ffn = FeedForward(d_model, dff, rate)
        self.layernorm1 = tf.keras.layers.LayerNormalization(epsilon=1e-6)
        self.layernorm2 = tf.keras.layers.LayerNormalization(epsilon=1e-6)

    def call(self, x, training):
        attn_output = self.mha(x, x, x, training)
        attn_output = self.layernorm1(x + attn_output)
        ffn_output = self.ffn(attn_output, training)
        return self.layernorm2(attn_output + ffn_output)

class DecoderLayer(tf.keras.layers.Layer):
    def __init__(self, d_model, num_heads, dff, rate=0.1):
        super(DecoderLayer, self).__init__()

        self.mha1 = MultiHeadAttention(d_model, num_heads)
        self.mha2 = MultiHeadAttention(d_model, num_heads)
        self.ffn = FeedForward(d_model, dff, rate)
        self.layernorm1 = tf.keras.layers.LayerNormalization(epsilon=1e-6)
        self.layernorm2 = tf.keras.layers.LayerNormalization(epsilon=1e-6)
        self.layernorm3 = tf.keras.layers.LayerNormalization(epsilon=1e-6)

    def call(self, x, enc_output, training):
        attn1_output = self.mha1(x, x, x, training)
        attn1_output = self.layernorm1(x + attn1_output)
        attn2_output = self.mha2(enc_output, enc_output, attn1_output, training)
        attn2_output = self.layernorm2(attn1_output + attn2_output)
        ffn_output = self.ffn(attn2_output, training)
        return self.layernorm3(attn2_output + ffn_output)

class MultiHeadAttention(tf.keras.layers.Layer):
    def __init__(self, d_model, num_heads=8, attention_probs_dropout_rate=0.1):
        super(MultiHeadAttention, self).__init__()
        self.num_heads = num_heads
        self.d_model = d_model

        assert d_model % num_heads == 0

        self.depth = d_model // num_heads

        self.wq = tf.keras.layers.Dense(d_model)
        self.wk = tf.keras.layers.Dense(d_model)
        self.wv = tf.keras.layers.Dense(d_model)

        self.attention_probs_dropout = tf.keras.layers.Dropout(attention_probs_dropout_rate)

    def call(self, q, k, v, training):
        batch_size = tf.shape(q)[0]

        q = self.wq(q)
        k = self.wk(k)
        v = self.wv(v)

        q = tf.reshape(q, (batch_size, -1, self.num_heads, self.depth))
        k = tf.reshape(k, (batch_size, -1, self.num_heads, self.depth))
        v = tf.reshape(v, (batch_size, -1, self.num_heads, self.depth))

        q = tf.transpose(q, perm=[0, 2, 1, 3])
        k = tf.transpose(k, perm=[0, 2, 1, 3])
        v = tf.transpose(v, perm=[0, 2, 1, 3])

        attention_weights = tf.matmul(q, k, transpose_b=True)
        attention_weights = attention_weights / tf.math.sqrt(tf.cast(self.depth, tf.float32))
        attention_weights = tf.nn.softmax(attention_weights)

        attention_weights = self.attention_probs_dropout(attention_weights, training=training)

        output = tf.matmul(attention_weights, v)

        output = tf.transpose(output, perm=[0, 2, 1, 3])
        output = tf.reshape(output, (batch_size, -1, self.d_model))

        return output

class FeedForward(tf.keras.layers.Layer):
    def __init__(self, d_model, dff, activation="relu", rate=0.1):
        super(FeedForward, self).__init__()
        self.dense1 = tf.keras.layers.Dense(dff, activation=activation)
        self.dense2 = tf.keras.layers.Dense(d_model)
        self.dropout = tf.keras.layers.Dropout(rate)

    def call(self, x, training):
        x = self.dense1(x)
        x = self.dropout(x, training=training)
        x = self.dense2(x)
        return x

class PositionalEncoding(tf.keras.layers.Layer):
    def __init__(self, d_model, dropout=0.1, max_len=5000):
        super(PositionalEncoding, self).__init__()
        self.pos_encoding = self.positional_encoding(max_len, d_model)
        self.dropout = tf.keras.layers.Dropout(rate=dropout)

    def call(self, x):
        x = x + self.pos_encoding[:tf.shape(x)[1], :]
        return self.dropout(x)

    def positional_encoding(self, position, d_model):
        angle_rads = position