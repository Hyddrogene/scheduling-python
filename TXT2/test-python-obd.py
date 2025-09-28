#!/usr/bin/env python3
import obd
import time

# --- Paramètres ---
# Laisse None pour autodétection, ou force le port:
# port = "/dev/ttyUSB0"        # USB
# port = "/dev/ttyACM0"        # USB (certains modèles)
# port = "/dev/rfcomm0"        # Bluetooth lié avec rfcomm
port = None

# Vitesse d'échange: 38400 ou 9600 selon clones; python-OBD gère souvent tout seul
baudrate = None  # ex: 38400 si besoin

print("Connexion OBD…")
connection = obd.OBD(port, baudrate=baudrate, fast=False, timeout=3.0)

if not connection.is_connected():
    raise SystemExit("Échec de connexion OBD. Vérifie le port, le contact et l’adaptateur.")

print("Connecté. Protocole:", connection.protocol_name())

# Quelques commandes utiles
CMDS = {
    "RPM": obd.commands.RPM,
    "SPEED": obd.commands.SPEED,
    "COOLANT_TEMP": obd.commands.COOLANT_TEMP,
    "ENGINE_LOAD": obd.commands.ENGINE_LOAD,
    "THROTTLE_POS": obd.commands.THROTTLE_POS,
    "FUEL_STATUS": obd.commands.FUEL_STATUS,
}

# Affiche ce qui est supporté
supported = connection.supported_commands
print("\nCommandes supportées (extrait) :")
for name, cmd in list(CMDS.items()):
    print(f" - {name}: {'OK' if cmd in supported else 'non'}")

# Lecture des DTC (codes défauts)
print("\nLecture des DTC…")
dtc_response = connection.query(obd.commands.GET_DTC)
if dtc_response.is_null():
    print("Pas de DTC ou non supporté.")
else:
    dtcs = dtc_response.value  # liste de tuples (code, description)
    if dtcs:
        for code, desc in dtcs:
            print(f" DTC: {code} — {desc}")
    else:
        print("Aucun DTC présent.")

print("\nStreaming des données temps réel (Ctrl+C pour arrêter)")
try:
    while True:
        line = []
        for name, cmd in CMDS.items():
            if cmd in supported:
                r = connection.query(cmd)
                if r.is_null():
                    val = "—"
                else:
                    val = str(r.value)  # unité formatée (ex: 85 degC, 2500 rpm)
            else:
                val = "N/S"  # non supporté
            line.append(f"{name}: {val}")
        print(" | ".join(line))
        time.sleep(1.0)
except KeyboardInterrupt:
    print("\nArrêt.")
finally:
    connection.close()
