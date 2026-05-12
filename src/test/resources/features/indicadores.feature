# language: pt
Funcionalidade: Testes de API ESG Inteligente

  Cenário: Listagem de Setores (GET)
    Quando eu consultar todos os setores
    Então o status code deve ser 200

Cenário: Validar erro ao cadastrar equipamento (Cenário Negativo)
    Quando eu cadastrar um equipamento com nome "Painel Solar" e potencia 400 para o setor 1
    Então o status code deve ser 400

  Cenário: Consultar Dashboard de Sustentabilidade (GET)
    Quando eu consultar indicadores de "2024-01-01" ate "2024-12-31"
    Então o status code deve ser 200
    E o contrato deve ser validado pelo arquivo "indicador-schema.json"

  Cenário: Exclusão lógica de Equipamento (DELETE)
    Quando eu deletar o equipamento com ID 1
    Então o status code deve ser 204