package com.procurement.notifierkafka.exception

class ParseNotificationException(notification: String) :
    RuntimeException("Error of convert 'JSON' to request body: '$notification'")
