package com.reactnativeldk
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import org.ldk.structs.*

fun handleResolve(promise: Promise, res: LdkCallbackResponses) {
    LdkEventEmitter.send(EventTypes.swift_log, "Success: ${res}")
    promise.resolve(res.toString());
}

fun handleReject(promise: Promise, ldkError: LdkErrors, error: Error? = null) {
    if (error !== null) {
        LdkEventEmitter.send(EventTypes.swift_log, "Error: ${ldkError}. Message: ${error.toString()}")
        promise.reject(ldkError.toString(), error);
    } else {
        LdkEventEmitter.send(EventTypes.swift_log, "Error: ${ldkError}")
        promise.reject(ldkError.toString(), ldkError.toString())
    }
}

fun ByteArray.hexEncodedString(): String {
    return joinToString("") { "%02x".format(it) }
}

fun String.hexa(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }
    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

val Invoice.asJson: WritableMap
    get() {
        val result = Arguments.createMap()
        val signedInv = into_signed_raw()
        val rawInvoice = signedInv.raw_invoice()

        result.putInt("amount_milli_satoshis", (amount_milli_satoshis() as Option_u64Z.Some).some.toInt())
        result.putString("description", rawInvoice.description()?.into_inner())
        result.putBoolean("check_signature",  signedInv.check_signature())
        result.putBoolean("is_expired",  is_expired)
        result.putInt("duration_since_epoch",  duration_since_epoch().toInt())
        result.putInt("expiry_time",  expiry_time().toInt())
        result.putInt("min_final_cltv_expiry",  min_final_cltv_expiry().toInt())
        result.putString("payee_pub_key", rawInvoice.payee_pub_key()?._a?.hexEncodedString())
        result.putString("recover_payee_pub_key", recover_payee_pub_key().hexEncodedString())
        result.putString("payment_hash", payment_hash().hexEncodedString())
        result.putString("payment_secret", payment_secret().hexEncodedString())
        result.putInt("timestamp", timestamp().toInt())
        result.putString("features", features()?.write()?.hexEncodedString())
        result.putInt("currency", currency().ordinal)
        result.putString("to_str", signedInv.to_str())

        return result
    }

val ChannelDetails.asJson: WritableMap
    get() {
        val result = Arguments.createMap()

        result.putString("channel_id", _channel_id.hexEncodedString())
        result.putBoolean("is_public", _is_public)
        result.putBoolean("is_usable", _is_usable)
        result.putBoolean("is_outbound", _is_outbound)
        result.putInt("balance_msat", _balance_msat.toInt())
        result.putString("counterparty", _counterparty.write().hexEncodedString())
        result.putString("funding_txo", if (_funding_txo != null) _funding_txo!!.write().hexEncodedString() else null)
        result.putString("channel_type", if (_channel_type != null) _channel_type!!.write().hexEncodedString() else null)
        result.putInt("user_channel_id", _user_channel_id.toInt())
        result.putInt("get_confirmations_required", (_confirmations_required as Option_u32Z.Some).some)
        (_short_channel_id as? Option_u64Z.Some)?.some?.toInt()
            ?.let { result.putInt("short_channel_id", it) } //Optional number
        result.putBoolean("is_funding_locked", _is_funding_locked)
        (_inbound_scid_alias as? Option_u64Z.Some)?.some?.toInt()
            ?.let { result.putInt("inbound_scid_alias", it) }
        (_inbound_scid_alias as? Option_u64Z.Some)?.some?.toInt()
            ?.let { result.putInt("inbound_payment_scid", it) }
        result.putInt("inbound_capacity_msat", _inbound_capacity_msat.toInt())
        result.putInt("outbound_capacity_msat", _outbound_capacity_msat.toInt())
        result.putInt("channel_value_satoshis", _channel_value_satoshis.toInt())
        (_force_close_spend_delay as? Option_u16Z.Some)?.some?.toInt()
            ?.let { result.putInt("force_close_spend_delay", it) }
        result.putInt("unspendable_punishment_reserve", (_unspendable_punishment_reserve as Option_u64Z.Some).some.toInt())

        return result
    }

val RouteHop.asJson: WritableMap
    get() {
        val hop = Arguments.createMap()
        hop.putHexString("pubkey", _pubkey)
        hop.putInt("fee_msat", _fee_msat.toInt())
        return hop
    }

fun WritableMap.putHexString(key: String, bytes: ByteArray?) {
    if (bytes != null) {
        putString(key, bytes.hexEncodedString())
    } else {
        putString(key, null)
    }
}

fun WritableArray.pushHexString(bytes: ByteArray) {
    pushString(bytes.hexEncodedString())
}