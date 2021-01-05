package com.tw.login.tools;

import java.util.List;

import com.google.protobuf.MessageLite;
import com.tw.login.proto.CsEnum.EnmCmdID;
import com.tw.login.proto.CsLogin.CSLoginReq;
import com.tw.login.proto.CsLogin.CSLoginRes;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PackDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // ��ȡ��ͷ�е�body����
        byte low = in.readByte();
        byte high = in.readByte();
        short s0 = (short) (low & 0xff);
        short s1 = (short) (high & 0xff);
        s1 <<= 8;
        short length = (short) (s0 | s1);

        // ��ȡ��ͷ�е�protobuf����
        byte low_type = in.readByte();
        byte high_type = in.readByte();
        short s0_type = (short) (low_type & 0xff);
        short s1_type = (short) (high_type & 0xff);
        s1_type <<= 8;
        short dataTypeId = (short) (s0_type | s1_type);

        // ����ɶ�����С��body���ȣ��ָ���ָ�룬�˳���
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        //��ʼ��ȡ����protobuf����
        ByteBuf bodyByteBuf = in.readBytes(length);
        byte[] array;
        //�����л����ݵ���ʼ��
        int offset;
        //�ɶ��������ֽڳ���
        int readableLen= bodyByteBuf.readableBytes();
        //��Ϊ�����������ݺͲ�������������������ʽ
        if (bodyByteBuf.hasArray()) {
            array = bodyByteBuf.array();
            offset = bodyByteBuf.arrayOffset() + bodyByteBuf.readerIndex();
        } else {
            array = new byte[readableLen];
            bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), array, 0, readableLen);
            offset = 0;
        }

        //�����л�
        MessageLite result = decodeBody(dataTypeId, array, offset, readableLen);
        out.add(result);
    }

    /**
     * ����Э�������Ӧ��protobuf����������Э������
     * @param _typeId
     * @param array
     * @param offset
     * @param length
     * @return
     * @throws Exception
     */
    public MessageLite decodeBody(int _typeId,byte[] array,int offset,int length) throws Exception{
        if(_typeId == EnmCmdID.CS_LOGIN_REQ_VALUE){
            return CSLoginReq.getDefaultInstance().getParserForType().parseFrom(array,offset,length);
        }
        else if(_typeId == EnmCmdID.CS_LOGIN_RES_VALUE){
            return CSLoginRes.getDefaultInstance().getParserForType().parseFrom(array,offset,length);
        }
        return null;
    }
}