package com.tw.login.tools;

import com.google.protobuf.MessageLite;
import com.tw.login.proto.CsEnum.EnmCmdID;
import com.tw.login.proto.CsLogin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 自定义编码器
 * @author linsh
 *
 */
public class PackEncoder extends MessageToByteEncoder<MessageLite> {

    /**
     * 传入协议数据，产生携带包头之后的数据
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageLite msg, ByteBuf out) throws Exception {
        // TODO Auto-generated method stub
        byte[] body = msg.toByteArray();
        byte[] header = encodeHeader(msg, (short)body.length);

        out.writeBytes(header);
        out.writeBytes(body);
        return;
    }

    /**
     * 获得一个协议头
     * @param msg
     * @param bodyLength
     * @return
     */
    private byte[] encodeHeader(MessageLite msg,short bodyLength){
        short _typeId = 0;
        if(msg instanceof CsLogin.CSLoginReq){
            _typeId = EnmCmdID.CS_LOGIN_REQ_VALUE;
        }else if(msg instanceof CsLogin.CSLoginRes){
            _typeId = EnmCmdID.CS_LOGIN_RES_VALUE;
        }
        //存放两个short数据
        byte[] header = new byte[4];
        //前两位放数据长度
        header[0] = (byte) (bodyLength & 0xff);
        header[1] = (byte) ((bodyLength >> 8) & 0xff);
        //后两个字段存协议id
        header[2] = (byte) (_typeId & 0xff);
        header[3] = (byte) ((_typeId >> 8) & 0xff);
        return header;
    }
}