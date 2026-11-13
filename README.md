# EntregaCEP — Roteiro 12

Aplicativo Android original em **Java**, para organizar pontos de entrega. Cada ponto reúne destinatário, endereço obtido por CEP, número, complemento e instruções de entrega. Projeto independente do roteiro 11; não utiliza o código do agenda-app.

## Funcionalidades

- Consulta real HTTPS à BrasilAPI: `GET https://brasilapi.com.br/api/cep/v1/{cep}` com Retrofit e cliente OkHttp (timeouts e tratamento de falhas).
- Cadastro exige consulta bem-sucedida do CEP. CEPs gerais sem rua permitem completar o endereço manualmente.
- Navegação entre listagem, cadastro/edição e lixeira.
- Room armazena os pontos no aparelho. Edição preserva o ID; exclusão lógica preenche `deletedAt`, sem apagar a linha. A lixeira permite restaurar.
- Banco e rede executam fora da interface, em executores; ViewModels e LiveData mantêm resultados nas mudanças de configuração.
- Uso local sem login. Só a consulta de CEP exige internet. Desinstalar ou limpar os dados apaga o banco local.

## Abrir no Android Studio

1. Clone ou baixe este repositório e abra a pasta que contém `settings.gradle` (File → Open).
2. Selecione **JDK 17** em Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK.
3. Instale o Android SDK Platform 35 quando solicitado e aguarde a sincronização.
4. Selecione um emulador ou aparelho Android 8.0/API 26 ou superior e clique em Run.

Versões fixadas: AGP 8.7.3, Gradle 8.9, Java 17, Room 2.6.1, Retrofit 2.11.0 e OkHttp 4.12.0.

## Arquitetura Limpa

`presentation → domain ← data`

- **domain**: modelos `DeliveryPoint` e `Address`, contrato `DeliveryRepository` e regras `DeliveryUseCases`. Sem dependência de Android, Room ou Retrofit.
- **data**: entidade/DAO/banco Room, DTO e interface Retrofit, implementação `RoomDeliveryRepository` e mapeamento entre modelos.
- **presentation**: Activities e ViewModels para exibir estado, receber entradas e chamar os casos de uso.
- **EntregaApp**: raiz de composição. Injeta as implementações por construtores; não é necessário Hilt para os requisitos desta atividade.

## Compilação e testes

Windows: `gradlew.bat assembleDebug testDebugUnitTest lintDebug`

Linux/macOS: `bash gradlew assembleDebug testDebugUnitTest lintDebug`

O workflow **Android Debug APK** compila, executa testes unitários e lint, publicando o artefato `EntregaCEP-debug` na página da execução em Actions. O APK gerado é `app/build/outputs/apk/debug/app-debug.apk`.

Testes unitários cobrem normalização/validação de CEP, cadastro inválido, soft delete/restauração e integração Retrofit/OkHttp com servidor HTTP de teste. O teste instrumentado `RoomPersistenceTest` reabre um banco em disco e verifica edição, exclusão lógica e restauração. Execute-o com aparelho/emulador conectado: `gradlew.bat connectedDebugAndroidTest`.

### Roteiro de demonstração manual

1. Novo ponto: nome “Recepção”, destinatário “Equipe”, CEP **01001000** → Consultar CEP. Deve mostrar Praça da Sé, Sé, São Paulo/SP.
2. Informe número e instruções, salve e confira a listagem.
3. Edite o número; feche e reabra o aplicativo para verificar persistência.
4. Mova para lixeira; feche e reabra. Deve continuar apenas na lixeira. Restaure e confira o retorno à lista.
5. Tente CEP curto, CEP inexistente e consulta sem internet. Deve mostrar erro e permitir tentar novamente, sem encerrar o aplicativo.

## Referências técnicas

- [BrasilAPI](https://brasilapi.com.br/docs)
- [Compatibilidade AGP 8.7 / Gradle 8.9 / JDK 17](https://developer.android.com/build/releases/agp-8-7-0-release-notes)
- [Room](https://developer.android.com/training/data-storage/room)
- [Retrofit](https://square.github.io/retrofit/)
