# deploy_forwarding_server.sh

# 포트 포워딩 설정 후

# export KAKAO_CLIENT_ID={secret}
# export ALLOWED_ORIGIN={webserver_origin} #default: http://localhost:3000
# ./deploy_forwarding_server.sh

# 안내에 따라
# 공인 ip, port 입력
# KAKAO_CLIENT_ID 및 카카오 리다이렉트 url 등록 확인

echo "[ 배포 스크립트 - 로컬 PC 외부 포워딩 서버 실행 ]"

read -p "포워딩된 공인 IP: " SERVER_IP
read -p "포워딩된 공인 port: " SERVER_PORT

export SPRING_SERVER_ADDRESS=0.0.0.0
export SPRING_SERVER_PORT=8080
export SPRING_PROFILES_ACTIVE=local
export KAKAO_REDIRECT_LOGIN="https://${SERVER_IP}:${SERVER_PORT}/api/oauth/kakao/login"

# 확인 절차
echo ""
echo "KAKAO_CLIENT_ID 확인!!: ${KAKAO_CLIENT_ID}"
echo "카카오 리다이렉트 URL: ${KAKAO_REDIRECT_LOGIN}"
echo "개발자 콘솔에서 위 리다이렉트 URL이 등록되었는지 확인하세요."
echo "https://developers.kakao.com/console/app/1285204/product/login"
echo "개발자 이메일: keroro06108@gmail.com"

echo ""
echo "Spring Boot 앱 빌드 중..."
./gradlew clean bootJar
JAR_PATH=$(find build/libs -name "*jar" | head -n 1)

if [ -z "$JAR_PATH" ]; then
  echo "JAR 파일을 찾을 수 없습니다. 빌드가 실패했을 수 있습니다."
  exit 1
fi

echo ""
echo "Spring Boot 앱 실행..."
java -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
     -Dspring.server.address=$SPRING_SERVER_ADDRESS \
     -Dspring.server.port=$SPRING_SERVER_PORT \
     -Dkakao.redirect.login=$KAKAO_REDIRECT_LOGIN \
     -Dallowed.origin=$ALLOWED_ORIGIN \
     -jar "$JAR_PATH"