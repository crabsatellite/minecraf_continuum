import os
import psutil
import subprocess
import time
import configparser
import sys

# Read configuration from config.ini
config = configparser.ConfigParser()
config.read('config.ini')

# Retrieve base directory from config file
BASE_DIR = config.get('Settings', 'base_dir')


def stop_minecraft():
    # Iterate over all running processes
    for process in psutil.process_iter(['pid', 'name']):
        try:
            # Check if the process name contains 'java' which is commonly used by Minecraft
            if 'java' in process.info['name'].lower():
                print(f"Terminating Minecraft process (PID: {process.info['pid']})")
                process.terminate()  # Gracefully terminate the process
                process.wait(timeout=10)  # Wait up to 10 seconds for process to terminate
        except (psutil.NoSuchProcess, psutil.AccessDenied, psutil.ZombieProcess):
            pass


def open_bat_file(version_dir):
    # Construct the full path to the .bat file
    bat_file = os.path.join(BASE_DIR, version_dir, 'PCL', 'LatestLaunch.bat')

    # Check if the .bat file exists
    if os.path.exists(bat_file):
        print(f"Opening {bat_file}")
        # Open the batch file using subprocess
        subprocess.Popen(bat_file, shell=True)
    else:
        print(f"Batch file not found: {bat_file}")


if __name__ == "__main__":
    # Check for command line argument for version directory
    if len(sys.argv) > 1:
        version_dir = sys.argv[1].strip()
        print(f"Using version directory from argument: {version_dir}")
    else:
        # Get the version directory from user input if no argument provided
        version_dir = input(
            "Enter the Minecraft version directory name (e.g., minecraft_secret_world_1.12.2): ").strip()

    # Stop any running Minecraft instances
    stop_minecraft()

    # Wait a moment to ensure all processes are closed
    time.sleep(2)

    # Open the specified .bat file for the given version directory
    open_bat_file(version_dir)
