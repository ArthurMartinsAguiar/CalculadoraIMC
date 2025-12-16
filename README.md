# 📱 Aplicativo de Cálculo de IMC

## 📌 Visão Geral

Este projeto consiste em um **aplicativo Android para cálculo do IMC (Índice de Massa Corporal) e outras métricas de saúde**, desenvolvido com foco em simplicidade, organização arquitetural e boa experiência do usuário.

O aplicativo permite cadastrar medições de IMC, visualizar detalhes completos do cálculo e manter um histórico organizado das medições realizadas.

---

## 🎯 Funcionalidades

* Cadastro de medições de IMC
* Cálculo automático do IMC
* Classificação do IMC (ex.: Magreza, Normal, Sobrepeso, Obesidade)
* Exibição de indicadores adicionais de saúde
* Histórico de medições
* Remoção de medições salvas

### 📊 Indicadores Calculados

* **IMC (Índice de Massa Corporal)**
* **Classificação do IMC**
* **Taxa Metabólica Basal (TMB)**
* **Peso ideal estimado** (fórmula de Devine)
* **Necessidade calórica diária**, considerando o nível de atividade física

---

## 🖥️ Telas do Aplicativo

### 1️⃣ Tela de Histórico de Medições

Lista todas as medições salvas.

**Cada item da lista contém:**

* Nome do usuário
* Valor do IMC
* Classificação
* Data da medição

**Ações disponíveis:**

* Seleção de itens
* Exclusão de medições
* Adição de nova medição via botão flutuante (FAB)

### 2️⃣ Tela "Adicionar IMC"

Responsável pela entrada de dados do usuário.

**Campos disponíveis:**

* Nome
* Sexo (Masculino / Feminino)
* Idade (anos)
* Altura (cm)
* Peso (kg)
* Nível de atividade física

**Ação principal:**

* Botão **Calcular**, que valida os dados e realiza os cálculos necessários.

---

### 3️⃣ Tela "Detalhes da Medição"

Exibe o resultado completo da medição selecionada.

**Informações exibidas:**

* Dados pessoais informados
* Data da medição
* Valor do IMC com destaque visual
* Classificação do IMC

**Indicadores adicionais:**

* Taxa Metabólica Basal (TMB)
* Peso ideal estimado
* Necessidade calórica diária

Essa tela centraliza todas as informações relevantes da medição, facilitando a compreensão do usuário.

---

## 🧱 Decisões de Arquitetura

O projeto segue boas práticas recomendadas para aplicações Android modernas.

### 🏗️ Arquitetura Utilizada

* **MVVM (Model-View-ViewModel)**

**Motivações:**

* Separação clara de responsabilidades
* Facilidade de manutenção
* Melhor testabilidade
* Organização do código conforme crescimento do projeto

---

### 📂 Organização de Pacotes

* `ui` → Telas e componentes visuais (Compose)
* `domain` → Regras de negócio (cálculo de IMC, TMB, peso ideal, etc.)
* `data` → Persistência de dados (Room)
* `model` → Modelos de dados
* `navigation` -> Navegação entre telas

---

### 🧮 Lógica de Cálculo

* O cálculo do IMC e demais indicadores é centralizado em uma **classe de domínio**, evitando lógica diretamente nas telas.
* Isso garante reutilização, clareza e facilidade de testes.

---

### 💾 Persistência de Dados

* As medições são armazenadas localmente utilizando **Room Database**.
* O acesso aos dados é feito via **DAO**, com suporte a funções `suspend`.
* O controle de estados assíncronos é feito no **ViewModel**, utilizando `viewModelScope`.

---

### 🎨 Interface e UX

* Interface desenvolvida com **Jetpack Compose**
* Tema claro/escuro variável conforme o sistema
* Componentes com boa hierarquia visual
* Destaque visual para resultados importantes (IMC e classificação)

---

## 🚀 Tecnologias Utilizadas

* Kotlin
* Jetpack Compose
* Room Database
* Coroutines
* MVVM
* Material Design 3

---

## 📈 Possíveis Melhorias Futuras

* Gráficos de evolução do IMC
* Exportação de dados
* Integração com APIs de saúde
* Recomendações personalizadas

---

## 📄 Licença

Projeto desenvolvido para fins educacionais.

---

✍️ **Autor:** Arthur Martins Aguiar e Eduardo Lordão Oliveira
