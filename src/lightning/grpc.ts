import { NativeModulesStatic } from 'react-native';

class GrpcAction {
  private readonly lnd: NativeModulesStatic;

  constructor(lndModule: NativeModulesStatic) {
    this.lnd = lndModule;
  }

  /**
   * Wrapper function to execute calls to the lnd grpc client.
   * @param method
   * @param body
   * @return {Promise<void>}
   */
  // async sendCommand(method: string, body: unknown): Promise<void> {
  //   const m = toCaps(method);
  //
  //   return this._lnrpcRequest(method, body);
  // }
  //
  // async _lnrpcRequest(method: string, body: Object) {
  //   try {
  //     method = toCaps(method);
  //     const req = this._serializeRequest(method, body);
  //     const response = await this.lnd.sendCommand(method, req);
  //
  //     let data = response.data;
  //     if (data == undefined) { //Some responses can be empty strings
  //       throw new Error("Invalid response");
  //     }
  //
  //     return this._deserializeResponse(method, data);
  //   } catch (err) {
  //     if (typeof err === 'string') {
  //       throw new Error(err);
  //     } else {
  //       throw err;
  //     }
  //   }
  // }
  //
  // _serializeRequest(method: string, body = {}) {
  //   const req = lnrpc[this._getRequestName(method)];
  //   //TODO validate rpc class exists
  //   const message = req.create(body);
  //   const buffer = req.encode(message).finish();
  //   return base64.fromByteArray(buffer);
  // }
}

export default GrpcAction;
