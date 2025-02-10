import os
import subprocess
import time
import requests
from typing import Dict, Optional


class ServiceManager:
    def __init__(self, socat_port: int = 8081, sleep_duration: int = 3) -> None:
        self.socat_port: int = socat_port
        self.sleep_duration: int = sleep_duration
        self.services: Dict[str, int] = {
            'wyl_1': 8082,
            'wyl_2': 8083
        }
        self.current_name: Optional[str] = None
        self.current_port: Optional[int] = None
        self.next_name: Optional[str] = None
        self.next_port: Optional[int] = None
#         self.compose_file_path = "../docker-compose.yml"  # Docker Compose 파일 경로

    def _find_current_service(self) -> None:
        cmd: str = f"ps aux | grep 'socat -t0 TCP-LISTEN:{self.socat_port}' | grep -v grep | awk '{{print $NF}}'"
        current_service: str = subprocess.getoutput(cmd)
        if not current_service:
            self.current_name, self.current_port = 'wyl_2', self.services['wyl_2']
        else:
            self.current_port = int(current_service.split(':')[-1])
            self.current_name = next((name for name, port in self.services.items() if port == self.current_port), None)

    def _find_next_service(self) -> None:
        self.next_name, self.next_port = next(
            ((name, port) for name, port in self.services.items() if name != self.current_name),
            (None, None)
        )

    def _remove_container(self, name: str) -> None:
        os.system(f"docker stop {name} 2> /dev/null")
        os.system(f"docker rm -f {name} 2> /dev/null")

    def _run_container(self, name: str, port: int) -> None:
        os.system(
            f"docker run -d --name={name} --restart unless-stopped -p {port}:8090 -e TZ=Asia/Seoul -v /dockerProjects/wyl/volumes/gen:/gen --pull always ghcr.io/backendschoolplus3th/jupiter-back")

    def _switch_port(self) -> None:
        cmd: str = f"ps aux | grep 'socat -t0 TCP-LISTEN:{self.socat_port}' | grep -v grep | awk '{{print $2}}'"
        pid: str = subprocess.getoutput(cmd)

        if pid:
            os.system(f"kill -9 {pid} 2>/dev/null")

        time.sleep(5)

        os.system(
            f"nohup socat -t0 TCP-LISTEN:{self.socat_port},fork,reuseaddr TCP:localhost:{self.next_port} &>/dev/null &")

    def _is_service_up(self, port: int) -> bool:
        url = f"http://127.0.0.1:{port}/actuator/health"
        try:
            response = requests.get(url, timeout=5)
            if response.status_code == 200 and response.json().get('status') == 'UP':
                return True
        except requests.RequestException:
            pass
        return False

#     def _is_compose_running(self) -> bool:
#         # 실행 중인 Docker Compose 컨테이너가 있는지 확인하는 함수
#         cmd = "docker-compose -f " + self.compose_file_path + " ps -q"
#         result = subprocess.getoutput(cmd)
#         return bool(result)  # 실행 중인 컨테이너 ID가 있으면 True 반환
#
#     def _run_docker_compose(self) -> None:
#         if not self._is_compose_running():  # 이미 실행 중이지 않으면 실행
#             print("Docker Compose is not running. Starting...")
#             cmd: str = f"docker-compose -f {self.compose_file_path} up -d --build"
#             result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
#             if result.returncode != 0:
#                 print(f"Error running docker-compose: {result.stderr}")
#             else:
#                 print("Docker Compose ran successfully.")
#         else:
#             print("Docker Compose is already running. Skipping restart.")

    def update_service(self) -> None:
        self._find_current_service()
        self._find_next_service()

        self._remove_container(self.next_name)
        self._run_container(self.next_name, self.next_port)

        while not self._is_service_up(self.next_port):
            print(f"Waiting for {self.next_name} to be 'UP'...")
            time.sleep(self.sleep_duration)

        self._switch_port()

        if self.current_name is not None:
            self._remove_container(self.current_name)

        print("Switched service successfully!")

        # Docker Compose 실행 추가
#         self._run_docker_compose()


if __name__ == "__main__":
    manager = ServiceManager()
    manager.update_service()