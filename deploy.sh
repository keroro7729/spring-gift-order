
echo "==== [1] 환경 점검 ===="

# .env 존재 확인 (비밀 키는 수동으로 미리 넣어둬야 함)
if [ ! -f .env ]; then
    echo "[ERROR] .env 파일이 현재 경로에 존재하지 않습니다. 비밀 환경변수를 직접 생성해주세요."
    exit 1
fi

# .env 파일 내 주석 제거 및 빈 줄 제외하고 환경변수 export
export_env_vars() {
    while IFS='=' read -r key value; do
        # 주석(#) 제거, 공백 제거, 빈 줄 무시
        if [[ "$key" =~ ^\s*#.*$ ]] || [[ -z "$key" ]]; then
            continue
        fi
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)
        export "$key"="$value"
    done < <(grep -v '^#' .env | grep -v '^\s*$')
}

export_env_vars

echo ""
echo "==== [2] 환경변수 export ===="

export SPRING_SERVER_ADDRESS=0.0.0.0
export SPRING_SERVER_PORT=8080
export SPRING_PROFILES_ACTIVE=local

SERVER_IP=$(curl -s http://checkip.amazonaws.com)
export SERVER_IP
export KAKAO_REDIRECT_LOGIN="http://${SERVER_IP}:${SPRING_SERVER_PORT}/api/oauth/kakao/login"

echo "카카오 리다이렉트 URL: ${KAKAO_REDIRECT_LOGIN}"
echo "개발자 콘솔에서 위 리다이렉트 URL이 등록되었는지 확인하세요."
echo "https://developers.kakao.com/console/app/1285204/product/login"
echo "개발자 이메일: keroro06108@gmail.com"


echo ""
echo "==== [3] 기존 프로세스 종료 ===="

APP_NAME="spring-gift-app"

PID=$(pgrep -f $APP_NAME)

if [ -n "$PID" ]; then
    echo "기존 실행 중인 프로세스 종료 중 (PID: $PID)..."
    kill -9 $PID
    echo "기존 프로세스 종료 완료"
else
    echo "기존 프로세스 없음"
fi


echo ""
echo "==== [4] 애플리케이션 빌드 및 실행 ===="

./gradlew clean build -x test

if [ $? -ne 0 ]; then
    echo "[ERROR] 빌드 실패"
    exit 1
fi

java -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar build/libs/*-SNAPSHOT.jar --spring.application.name=$APP_NAME

