package com.github.jitpack.synocheck.util;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;

public abstract class OIDGetter {

  public static String getSingleOID(Snmp snmp, CommunityTarget communityTarget, String oid)
      throws IOException {
    String retVal;
    PDU pdu = new PDU();
    pdu.add(new VariableBinding(new OID(oid)));
    pdu.setType(PDU.GET);
    pdu.setRequestID(new Integer32(1));
    ResponseEvent response = snmp.get(pdu, communityTarget);
    if (response != null)
    {
      PDU responsePDU = response.getResponse();
      if (responsePDU != null)
      {
        int errorStatus = responsePDU.getErrorStatus();
        int errorIndex = responsePDU.getErrorIndex();
        String errorStatusText = responsePDU.getErrorStatusText();
        if (errorStatus == PDU.noError)
        {
          retVal = responsePDU.getVariableBindings().elementAt(0).toValueString();
        }
        else
        {
          retVal = "Error " + errorStatus + "/" + errorIndex + ": " + errorStatusText;
        }
      }
      else
      {
        retVal = "Error: Response PDU is null";
      }
    }
    else
    {
      retVal = "Error: Agent Timeout... ";
    }

    return retVal;
  }
}
