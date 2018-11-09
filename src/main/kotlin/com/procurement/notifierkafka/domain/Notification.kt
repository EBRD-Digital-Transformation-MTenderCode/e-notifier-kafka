package com.procurement.notifierkafka.domain

import java.util.*

data class Notification(val platformId: UUID, val operationId: UUID, val message: String)

/* Sample
{
    "platformId": "dbb8c62a-5e81-44a5-b8c9-22b667f730bb",
    "operationId": "72bd9399-bf15-48d6-b85f-e3341da02191",
    "message": "message 108"
}
*/
