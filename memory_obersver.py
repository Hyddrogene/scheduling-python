import subprocess
import time

def monitor_process(pid):
    duration = 3600  # Durée en secondes (1 heure)
    interval = 10     # Intervalle entre chaque exécution en secondes

    end_time = time.time() + duration

    while time.time() < end_time:
        # Exécute la commande "ps aux | grep <PID>"
        command = f"ps aux | grep {pid}"
        result = subprocess.run(command, shell=True, stdout=subprocess.PIPE, text=True)

        # Affiche le résultat de la commande
        print(result.stdout)

        # Attend l'intervalle spécifié avant la prochaine exécution
        time.sleep(interval)

if __name__ == "__main__":
    # Remplacez <PID> par le PID du processus que vous souhaitez surveiller
    process_pid = "votre_pid_ici"

    monitor_process(process_pid)
