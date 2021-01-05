package com.tw.login.tools;

import com.google.protobuf.MessageLite;
import com.tw.login.proto.CsEnum.EnmCmdID;
import com.tw.login.proto.CsLogin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * �Զ��������
 * @author linsh
 *
 */
public class PackEncoder extends MessageToByteEncoder<MessageLite> {

    /**
     * ����Э�����ݣ�����Я����ͷ֮�������
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
     * ���һ��Э��ͷ
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
        //�������short����
        byte[] header = new byte[4];
        //ǰ��λ�����ݳ���
        header[0] = (byte) (bodyLength & 0xff);
        header[1] = (byte) ((bodyLength >> 8) & 0xff);
        //�������ֶδ�Э��id
        header[2] = (byte) (_typeId & 0xff);
        header[3] = (byte) ((_typeId >> 8) & 0xff);
        return header;
    }
}