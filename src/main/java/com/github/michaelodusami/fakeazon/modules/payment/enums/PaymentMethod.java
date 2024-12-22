package com.github.michaelodusami.fakeazon.modules.payment.enums;

public enum PaymentMethod {

    // Credit/Debit Card Payments
    VISA,
    MASTERCARD,
    AMERICAN_EXPRESS,
    DISCOVER,

    // Digital Wallets
    PAYPAL,
    APPLE_PAY,
    GOOGLE_PAY,
    AMAZON_PAY,

    // Bank Transfers
    ACH,
    WIRE_TRANSFER,
    DIRECT_DEBIT,

    // Buy Now, Pay Later
    AFFIRM,
    KLARNA,
    AFTERPAY,

    // Cryptocurrencies
    BITCOIN,
    ETHEREUM,
    LITECOIN,

    // Cash/Other
    CASH_ON_DELIVERY,
    CHECK,
    MONEY_ORDER
}
