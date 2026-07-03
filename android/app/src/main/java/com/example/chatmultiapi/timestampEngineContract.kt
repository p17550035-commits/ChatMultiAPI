// timestampenginecontract.kt — v1.0.0

package com.chatmultiapi.godmode.engine

/**
 * TimestampEngineContract — v1
 * -----------------------------
 * Real wiring contract for timestamp generation + conversion.
 * Implementations must provide:
 * - local timestamp (EDT display)
 * - utc timestamp (storage + routing)
 * - conversion between local <-> utc
 * - validation for crash recovery + metadata
 */
interface TimestampEngineContract {

    /** Returns current local timestamp (MM/DD/YYYY HH:MM EDT) */
    fun nowLocal(): String

    /** Returns current UTC timestamp (YYYY-MM-DDTHH:MM:SSZ) */
    fun nowUtc(): String

    /** Converts a local timestamp to UTC */
    fun convertLocalToUtc(local: String): String

    /** Converts a UTC timestamp to local */
    fun convertUtcToLocal(utc: String): String

    /** Validates timestamp pair for metadata + crash recovery */
    fun validate(local: String, utc: String): Boolean
}

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 2.2 timestampenginecontract.kt (timestamp wiring — contract)
version: 1.0.0
origin: timestampenginecontract.kt
mode: embedded editor mode

dependencies:
- bubblemodel.kt
- bubbleadapter.kt
- chatactivity.kt

ml tags:
- timestamp_engine
- data_contract
- godmode_core
- v1_ruleset

end of file :: godmode :: chatmultiapi
================================================================================
