/**************************************************************************************************
 Filename:       firmwareEntriesParser.java

 Copyright (c) 2013 - 2015 Texas Instruments Incorporated

 All rights reserved not granted herein.
 Limited License.

 Texas Instruments Incorporated grants a world-wide, royalty-free,
 non-exclusive license under copyrights and patents it now or hereafter
 owns or controls to make, have made, use, import, offer to sell and sell ("Utilize")
 this software subject to the terms herein.  With respect to the foregoing patent
 license, such license is granted  solely to the extent that any such patent is necessary
 to Utilize the software alone.  The patent license shall not apply to any combinations which
 include this software, other than combinations with devices manufactured by or for TI ('TI Devices').
 No hardware patent is licensed hereunder.

 Redistributions must preserve existing copyright notices and reproduce this license (including the
 above copyright notice and the disclaimer and (if applicable) source code license limitations below)
 in the documentation and/or other materials provided with the distribution

 Redistribution and use in binary form, without modification, are permitted provided that the following
 conditions are met:

 * No reverse engineering, decompilation, or disassembly of this software is permitted with respect to any
 software provided in binary form.
 * any redistribution and use are licensed by TI for use only with TI Devices.
 * Nothing shall obligate TI to provide you with source code for the software licensed and provided to you in object code.

 If software source code is provided to you, modification and redistribution of the source code are permitted
 provided that the following conditions are met:

 * any redistribution and use of the source code, including any resulting derivative works, are licensed by
 TI for use only with TI Devices.
 * any redistribution and use of any object code compiled from the source code and any resulting derivative
 works, are licensed by TI for use only with TI Devices.

 Neither the name of Texas Instruments Incorporated nor the names of its suppliers may be used to endorse or
 promote products derived from this software without specific prior written permission.

 DISCLAIMER.

 THIS SOFTWARE IS PROVIDED BY TI AND TI'S LICENSORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL TI AND TI'S LICENSORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.


 **************************************************************************************************/
package com.example.ti.util;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class firmwareEntriesParser {
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFw(parser);
        } finally {
            in.close();
        }

    }

    private List readFw(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "FirmwareEntries");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("FirmwareEntry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    public tiFirmwareEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "FirmwareEntry");
        Boolean Custom = false;
        String WirelessStandard = "";
        String Type = "";
        int OADAlgo = 0;
        String BoardType = "";
        float RequiredVersionRev = 0.0f;
        Boolean SafeMode = false;
        float Version = 0.0f;
        String Filename = "";
        String DevPack = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Custom")) {
                Custom = readBoolean(parser, "Custom");
            } else if (name.equals("WirelessStandard")) {
                WirelessStandard = readTag(parser, "WirelessStandard");
            } else if (name.equals("Type")) {
                Type = readTag(parser, "Type");
            } else if (name.equals("OADAlgo")) {
                OADAlgo = readInt(parser, "OADAlgo");
            } else if (name.equals("BoardType")) {
                BoardType = readTag(parser, "BoardType");
            } else if (name.equals("RequiredVersionRev")) {
                RequiredVersionRev = readFloat(parser, "RequiredVersionRev");
            } else if (name.equals("SafeMode")) {
                SafeMode = readBoolean(parser, "SafeMode");
            } else if (name.equals("Version")) {
                Version = readFloat(parser, "Version");
            } else if (name.equals("Filename")) {
                Filename = readTag(parser, "Filename");
            } else if (name.equals("DevPack")) {
                DevPack = readTag(parser, "DevPack");
            }
        }
        tiFirmwareEntry entry = new tiFirmwareEntry(Filename,Custom, WirelessStandard, Type,
        OADAlgo,BoardType,RequiredVersionRev, SafeMode,
        Version,DevPack);
        return entry;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    private String readTag(XmlPullParser parser,String tag) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String wS = "";
        if (parser.next() == XmlPullParser.TEXT) {
            wS = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return wS;
    }

    private boolean readBoolean(XmlPullParser parser,String tag) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        boolean temp = false;
        if (parser.next() == XmlPullParser.TEXT) {
            temp = Boolean.getBoolean(parser.getText());
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return temp;
    }

    private float readFloat(XmlPullParser parser,String tag) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        float temp = 0.0f;
        if (parser.next() == XmlPullParser.TEXT) {
            temp = Float.parseFloat(parser.getText());
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return temp;
    }
    private int readInt(XmlPullParser parser,String tag) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        int temp = 0;
        if (parser.next() == XmlPullParser.TEXT) {
            temp = Integer.parseInt(parser.getText());
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return temp;
    }
}
