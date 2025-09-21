-- Criar schema se não existir
IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'payment')
BEGIN
EXEC('CREATE SCHEMA payment');
END
GO

-- Criar tabela de provedores de pagamento
IF OBJECT_ID('payment.payment_provider', 'U') IS NULL
BEGIN
CREATE TABLE payment.payment_provider (
                                          provider_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                          name NVARCHAR(100) NOT NULL,
                                          integration_type NVARCHAR(50) NOT NULL, -- CARD, BOLETO, PIX, MULTI
                                          status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                                          created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                                          updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
END
GO

-- Criar tabela de empresas/clientes
IF OBJECT_ID('payment.payment_company', 'U') IS NULL
BEGIN
CREATE TABLE payment.payment_company (
                                         company_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                         name NVARCHAR(150) NOT NULL,
                                         document NVARCHAR(20) NOT NULL, -- CPF ou CNPJ
                                         alias NVARCHAR(50) NULL,        -- Identificador curto / apelido
                                         status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                                         created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                                         updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
END
GO

CREATE TABLE payment.produto_externo (
                                         id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                         external_produto_id NVARCHAR(150) NOT NULL, -- id do produto no sistema externo
                                         client_id_reference NVARCHAR(150) NOT NULL, -- referência ao produto interno / sistema
                                         created_at DATETIME2 DEFAULT SYSDATETIME(),
                                         company_id BIGINT NOT NULL,
                                         amount DECIMAL(15,5) NOT NULL,
                                         CONSTRAINT fk_produto_company FOREIGN KEY (company_id)
                                             REFERENCES payment.payment_company (company_id)
);

IF OBJECT_ID('payment.payment_transaction', 'U') IS NULL
BEGIN
CREATE TABLE payment.payment_transaction (
                                             transaction_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                             provider_id BIGINT NOT NULL,
                                             external_id NVARCHAR(100) NOT NULL, -- ID do provedor
                                             product_id BIGINT NOT NULL,          -- FK para produto
                                             amount DECIMAL(15,5) NOT NULL,
                                             currency NVARCHAR(10) NOT NULL DEFAULT 'BRL',
                                             payment_method NVARCHAR(20) NOT NULL, -- CARD, BOLETO, PIX
                                             status NVARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                             created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                                             updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
                                             client_reference_id NVARCHAR(100) NOT NULL
                                             CONSTRAINT fk_provider FOREIGN KEY (provider_id)
                                                 REFERENCES payment.payment_provider (provider_id),
                                             CONSTRAINT fk_product FOREIGN KEY (product_id)
                                                 REFERENCES payment.produto_externo (id),
);
END
GO

INSERT INTO payment.payment_company (name, document, alias)
VALUES ('Idecan', '04236076000171', 'IDC');

INSERT INTO payment.payment_provider (name, integration_type)
VALUES ('Conjo', 'MULTI');