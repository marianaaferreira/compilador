# 📌 Fluxo para Criar uma Nova Branch, Commitar e Enviar para o GitHub

Este guia explica como criar uma branch baseada na `main`, realizar alterações, comitar e enviar para o repositório remoto no GitHub.

---

## ✅ 1. Garantir que está na `main` e atualizada

```bash
git checkout main
git pull origin main
```

---

## ✅ 2. Criar uma nova branch baseada na `main`

```bash
git checkout -b nome-da-nova-branch
```

**Exemplo:**

```bash
git checkout -b feature/login
```

---

## ✅ 3. Fazer alterações nos arquivos

Edite os arquivos desejados no seu editor ou IDE.

---

## ✅ 4. Verificar as modificações

```bash
git status
```

---

## ✅ 5. Adicionar arquivos ao stage

Adicionar todos os arquivos alterados:

```bash
git add .
```

Ou adicionar arquivos específicos:

```bash
git add caminho/do/arquivo.ext
```

---

## ✅ 6. Criar o commit

```bash
git commit -m "Mensagem descritiva das alterações"
```

**Exemplo:**

```bash
git commit -m "Adiciona tela de login e validação de usuário"
```

---

## ✅ 7. Enviar a branch para o repositório remoto (GitHub)

```bash
git push -u origin nome-da-nova-branch
```

Após o primeiro envio, nas próximas vezes pode usar apenas:

```bash
git push
```

---

## ✅ 8. (Opcional) Criar um Pull Request no GitHub

Após enviar a branch, acesse o repositório no GitHub. Você verá a opção:

**“Compare & pull request”** → clique e siga os passos para revisão e merge.

---

Se quiser que eu altere ou adicione mais seções, é só avisar!

