# Git Hooks 설정 가이드

이 프로젝트는 커밋 메시지 컨벤션과 브랜치 네이밍 컨벤션을 자동으로 검사하는 Git Hooks를 사용합니다.

## 🚀 설정 방법

### 방법 1: Maven 플러그인 사용 (권장)

프로젝트를 빌드하면 자동으로 Git Hooks가 설치됩니다:

```bash
# Maven 빌드 시 자동으로 Git Hooks 설치
./mvnw clean install
```

또는

```bash
mvn clean install
```

### 방법 2: 수동 설치

```bash
# Git Hooks 디렉토리를 Git hooks 경로로 설정
git config core.hooksPath .githooks

# 실행 권한 부여 (macOS/Linux)
chmod +x .githooks/commit-msg
chmod +x .githooks/pre-push
```

### 방법 3: 심볼릭 링크 사용

```bash
# .git/hooks 디렉토리에 심볼릭 링크 생성
ln -s ../../.githooks/commit-msg .git/hooks/commit-msg
ln -s ../../.githooks/pre-push .git/hooks/pre-push

# 실행 권한 부여 (macOS/Linux)
chmod +x .git/hooks/commit-msg
chmod +x .git/hooks/pre-push
```

## 📋 Git Hooks 설명

### commit-msg

커밋 메시지가 컨벤션을 따르는지 검사합니다.

**검사 항목:**
- 커밋 메시지 형식: `<type>(<scope>): <subject>`
- Type이 유효한지 확인 (feat, fix, docs, style, refactor, test, chore, perf, ci, build)
- Subject가 비어있지 않은지 확인
- Subject가 50자를 초과하지 않는지 경고

**예시:**
```bash
# ✅ 올바른 커밋 메시지
git commit -m "feat(controller): 사용자 목록 페이지네이션 추가"

# ❌ 잘못된 커밋 메시지
git commit -m "수정"
git commit -m "fix bug"
```

### pre-push

푸시 전에 브랜치 네이밍 컨벤션을 검사합니다.

**검사 항목:**
- 브랜치 이름이 컨벤션을 따르는지 확인
- 허용된 브랜치: `main`, `develop`, `master`
- 허용된 패턴: `feature/*`, `bugfix/*`, `hotfix/*`, `release/*`

**예시:**
```bash
# ✅ 올바른 브랜치 이름
git checkout -b feature/user-pagination
git checkout -b bugfix/user-id-missing
git checkout -b hotfix/security-patch
git checkout -b release/1.0.0

# ❌ 잘못된 브랜치 이름
git checkout -b Feature
git checkout -b fix
git checkout -b new-branch
```

## 🔧 문제 해결

### Git Hooks가 실행되지 않는 경우

1. **실행 권한 확인 (macOS/Linux)**
   ```bash
   chmod +x .githooks/commit-msg
   chmod +x .githooks/pre-push
   ```

2. **Git 설정 확인**
   ```bash
   git config core.hooksPath
   # 출력: .githooks (또는 .git/hooks)
   ```

3. **수동으로 Git 설정**
   ```bash
   git config core.hooksPath .githooks
   ```

### Windows에서 실행 권한 문제

Windows에서는 실행 권한이 자동으로 설정되지만, 문제가 발생하면 Git Bash를 사용하세요.

### Git Hooks 건너뛰기 (비권장)

긴급한 경우에만 사용하세요:

```bash
# 커밋 메시지 검사 건너뛰기
git commit --no-verify -m "message"

# 푸시 전 검사 건너뛰기
git push --no-verify
```

## 📚 참고 자료

- [Git Hooks 공식 문서](https://git-scm.com/docs/githooks)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [CONTRIBUTING.md](CONTRIBUTING.md) - 커밋 및 브랜치 컨벤션 상세 설명

