# deploy_localhost_server.sh

# export KAKAO_CLIENT_ID={secret}
# export ALLOWED_ORIGIN={webserver_origin} #default: http://localhost:3000
# ./deploy_forwarding_server.sh

echo "localhost:8080 내부 테스트 서버 배포"

export SPRING_SERVER_ADDRESS=localhost
export SPRING_SERVER_PORT=8080
export SPRING_PROFILES_ACTIVE=local

echo "KAKAO_CLIENT_ID 확인!!: ${KAKAO_CLIENT_ID}"

echo "Spring Boot 앱 빌드 중..."
./gradlew clean bootJar
JAR_PATH=$(find build/libs -name "*jar" | head -n 1)

if [ -z "$JAR_PATH" ]; then
  echo "JAR 파일을 찾을 수 없습니다. 빌드가 실패했을 수 있습니다."
  exit 1
fi

echo "Spring Boot 앱 실행..."
java -jar "$JAR_PATH"