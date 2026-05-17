## Настройка репозитория
### 1. Необходимые секреты

`DOCKER_HUB_USERNAME:` Ваше имя пользователя на Docker Hub

`DOCKER_HUB_TOKEN:` Токен доступа к Docker Hub

### 2. Добавление секретов в GitHub

1. Перейдите на страницу вашего репозитория на GitHub.
2. Откройте **Settings** - **Secrets and variables** - **Actions**.
3. Нажмите кнопку **New repository secret**.
4. Добавьте каждый секрет по отдельности, указав имя и значение.

### 3. Получение токена Docker Hub

1. **Зайдите на Docker Hub:** https://hub.docker.com/
2. **Перейдите в настройки аккаунта:** Нажмите на аватар - **Account Settings**
3. **Выберите раздел Settings:** В левом меню выберите **Personal access tokens**
4. **Создайте новый токен:** Нажмите **Generate new token**

5. **Настройте токен:**
   - **Access token:** `github-actions`
   - **Access permissions:** Выберите **Read & Write**

6. **Скопируйте токен**: Нажмите **Generate** и скопируйте сгенерированный токен.

### 4. Добавление секретов в GitHub

1. В репозитории перейдите в **Settings** - **Secrets and variables** - **Actions**
2. Нажмите  **New repository secret:**
3. Добавьте два секрета:
   - **Первый секрет**:
   - Name: `DOCKER_HUB_USERNAME`
   - Value: (ваше имя пользователя Docker Hub)
   - **Второй  секрет**:
   - Name: `DOCKER_HUB_TOKEN`
   - Value: (скопированный токен)