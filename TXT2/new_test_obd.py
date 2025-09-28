import obd

print("Connexion OBD…")

# force ton port série USB
#connection = obd.OBD("/dev/ttyUSB0", fast=False, timeout=5)connection = obd.OBD("/dev/ttyUSB0", baudrate=38400, fast=False)
connection = obd.OBD("/dev/ttyUSB0", baudrate=9600, fast=False)


if not connection.is_connected():
    print("❌ Échec de connexion : vérifie que le contact est sur ON")
    exit(1)

print("✅ Connecté au véhicule")
print("Protocole:", connection.protocol_name())

print("\n--- Test de quelques commandes ---")
tests = [
    obd.commands.RPM,
    obd.commands.SPEED,
    obd.commands.COOLANT_TEMP,
    obd.commands.ENGINE_LOAD,
]

for cmd in tests:
    if cmd in connection.supported_commands:
        r = connection.query(cmd)
        print(f"{cmd.name}: {r.value}")
    else:
        print(f"{cmd.name}: non supporté par ce véhicule")

connection.close()
