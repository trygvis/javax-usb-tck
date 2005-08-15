;;-----------------------------------------------------------------------------
;;      File:           dscr.a51
;;      Contents:       This file contains descriptor data tables.
;;
;;      Copyright (c) 2003 International Business Machines Corporation. All Rights Reserved.
;;      Copyright (c) 1997 AnchorChips, Inc. All rights reserved
;;-----------------------------------------------------------------------------
 
;;-----------------------------------------------------------------------------
;;This descriptor table is used for the JSR80 Java(TM) USB API TCK.
;;This set of descriptors is for use by all TCK images.
;;The build batch file sets the environment variable,  TOPOLOGY, BULKINT, or
;;ISO, required to select the correct descriptors for an image.
;;Note:  Some descriptors may not be supported by the sample device driver that
;;comes with the EZ-USB FX Development Kit.  Therefore, the produce ID used
;;for the topology test image is one that is not recognized by the sample driver.
;;-----------------------------------------------------------------------------
 
DSCR_DEVICE     equ     1       ;; Descriptor type: Device
DSCR_CONFIG     equ     2       ;; Descriptor type: Configuration
DSCR_STRING     equ     3       ;; Descriptor type: String
DSCR_INTRFC     equ     4       ;; Descriptor type: Interface
DSCR_ENDPNT     equ     5       ;; Descriptor type: Endpoint
 
ET_CONTROL      equ     0       ;; Endpoint type: Control
ET_ISO          equ     1       ;; Endpoint type: Isochronous
ET_BULK         equ     2       ;; Endpoint type: Bulk
ET_INT          equ     3       ;; Endpoint type: Interrupt
 
EP1INSZ         equ     64
EP1OUTSZ        equ     64
EP2INSZ         equ     8
EP2OUTSZ        equ     8
EP3INSZ         equ     64
EP3OUTSZ        equ     64
EP4INSZ         equ     8
EP4OUTSZ        equ     8
EP5INSZ         equ     16
EP5OUTSZ        equ     16
EP6INSZ         equ     32
EP6OUTSZ        equ     32
EP7INSZ         equ     64
EP7OUTSZ        equ     64
 
EP8INSZ         equ     16
EP8OUTSZ        equ     16
EP9INSZ         equ     16
EP9OUTSZ        equ     16
EP10INSZ        equ     32
EP10OUTSZ       equ     32
EP11INSZ        equ     32
EP11OUTSZ       equ     32
EP12INSZ        equ     64
EP12OUTSZ       equ     64
EP13INSZ        equ     64
EP13OUTSZ       equ     64
EP14INSZ        equ    128
EP14OUTSZ       equ    128
EP15INSZ        equ     64
EP15OUTSZ       equ     64
 
EP1023OUTSZL      equ     0FFh
EP1023OUTSZH      equ     03h
 
 
 
public          DeviceDscr, ConfigDscr, StringDscr, UserDscr
 
DSCR    SEGMENT CODE
 
;;-----------------------------------------------------------------------------
;; Global Variables
;;-----------------------------------------------------------------------------
                rseg DSCR               ;; locate the descriptor table in on-part memory.
 
DeviceDscr:
                db      deviceDscrEnd-DeviceDscr                ;; Descriptor length
                db      DSCR_DEVICE     ;; Decriptor type
                dw      1001H           ;; Specification Version (BCD)
                db      0FFH             ;; Device class
                db      0FFH             ;; Device sub-class
                db      0FFH             ;; Device sub-sub-class
                db      64              ;; Maximum packet size
                dw      4705H           ;; Vendor ID
$if (TOPOLOGY)
                dw      01FFH           ;; Product ID (not recognized by ezusb.sys)
$else
                dw      0210H           ;; Product ID (sample device)
$endif
                dw      0001H           ;; Product version ID
                db      1               ;; Manufacturer string index
                db      2               ;; Product string index
                db      3               ;; Serial number string index
$if (TOPOLOGY)
                db      2               ;; Number of configurations
$else
                db      1               ;; Number of configurations
$endif
deviceDscrEnd:
 
ConfigDscr:
                db      ConfigDscrEnd-ConfigDscr                ;; Descriptor length
                db      DSCR_CONFIG     ;; Descriptor type
                db      (EndConfig1-ConfigDscr) mod 256 ;; Config + End Points length (LSB)
                db      (EndConfig1-ConfigDscr) / 256   ;; Config + End Points length (MSB)
$if (TOPOLOGY)
                db      2               ;; Number of interfaces
$else
                db      1               ;; Number of interfaces
$endif
                db      1               ;; Configuration value
                db      4               ;; Configuration string index
                db      10100000b       ;; Attributes (b7 - buspwr, b6 - selfpwr, b5 - rwu)
                db      50              ;; Power requirement (div 2 ma)
ConfigDscrEnd:
 
IntrfcDscr:
                db      IntrfcDscrEnd-IntrfcDscr                ;; Descriptor length
                db      DSCR_INTRFC     ;; Descriptor type
                db      0               ;; Zero-based index of this interface
                db      0               ;; Alternate setting
$if (TOPOLOGY)
                db      30               ;; Number of end points
$elseif (BULKINT)
                db      8               ;; Number of end points
$elseif (ISO)
                db      16               ;; Number of end points
$endif
                db      0ffH            ;; Interface class
                db      00H             ;; Interface sub class
                db      00H             ;; Interface sub sub class
                db      6               ;; Interface descriptor string index
IntrfcDscrEnd:
 
$if (TOPOLOGY)
Ep1InDscr:
                db      Ep1InDscrEnd-Ep1InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      81H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP1INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep1InDscrEnd:
 
Ep1OutDscr:
                db      Ep1OutDscrEnd-Ep1OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      01H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP1INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep1OutDscrEnd:
 
Ep2InDscr:
                db      Ep2InDscrEnd-Ep2InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      82H             ;; Endpoint number, and direction
                db      ET_INT         ;; Endpoint type
                db      EP2INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep2InDscrEnd:
 
Ep2OutDscr:
                db      Ep2OutDscrEnd-Ep2OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      02H             ;; Endpoint number, and direction
                db      ET_INT         ;; Endpoint type
                db      EP2INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep2OutDscrEnd:
 
Ep3InDscr:
                db      Ep3InDscrEnd-Ep3InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      83H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP3INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep3InDscrEnd:
 
Ep3OutDscr:
                db      Ep3OutDscrEnd-Ep3OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      03H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP3INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep3OutDscrEnd:
 
Ep4InDscr:
                db      Ep4InDscrEnd-Ep4InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      84H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP4INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep4InDscrEnd:
 
Ep4OutDscr:
                db      Ep4OutDscrEnd-Ep4OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      04H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP4INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep4OutDscrEnd:
 
Ep5InDscr:
                db      Ep5InDscrEnd-Ep5InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      85H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP5INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep5InDscrEnd:
 
Ep5OutDscr:
                db      Ep5OutDscrEnd-Ep5OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      05H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP5INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep5OutDscrEnd:
 
Ep6InDscr:
                db      Ep6InDscrEnd-Ep6InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      86H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP6INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep6InDscrEnd:
 
Ep6OutDscr:
                db      Ep6OutDscrEnd-Ep6OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      06H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP6INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep6OutDscrEnd:
 
Ep7InDscr:
                db      Ep7InDscrEnd-Ep7InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      87H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP7INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep7InDscrEnd:
 
Ep7OutDscr:
                db      Ep7OutDscrEnd-Ep7OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      07H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP7INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep7OutDscrEnd:
$endif
 
$if ((ISO) | (TOPOLOGY))
Ep8InDscr:
                db      Ep8InDscrEnd-Ep8InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      88H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP8INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep8InDscrEnd:
 
Ep8OutDscr:
                db      Ep8OutDscrEnd-Ep8OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      08H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP8OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep8OutDscrEnd:
 
Ep9InDscr:
                db      Ep9InDscrEnd-Ep9InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      89H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP9INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep9InDscrEnd:
 
Ep9OutDscr:
                db      Ep9OutDscrEnd-Ep9OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      09H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP9OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep9OutDscrEnd:
 
Ep10InDscr:
                db      Ep10InDscrEnd-Ep10InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      8AH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP10INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep10InDscrEnd:
 
Ep10OutDscr:
                db      Ep10OutDscrEnd-Ep10OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      0AH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP10OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep10OutDscrEnd:
 
Ep11InDscr:
                db      Ep11InDscrEnd-Ep11InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      8BH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP11INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep11InDscrEnd:
 
Ep11OutDscr:
                db      Ep11OutDscrEnd-Ep11OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      0BH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP11OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep11OutDscrEnd:
 
Ep12InDscr:
                db      Ep12InDscrEnd-Ep12InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      8CH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP12INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep12InDscrEnd:
 
Ep12OutDscr:
                db      Ep12OutDscrEnd-Ep12OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      0CH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP12OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep12OutDscrEnd:
 
Ep13InDscr:
                db      Ep13InDscrEnd-Ep13InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      8DH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP13INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep13InDscrEnd:
 
Ep13OutDscr:
                db      Ep13OutDscrEnd-Ep13OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      0DH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP13OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep13OutDscrEnd:
 
Ep14InDscr:
                db      Ep14InDscrEnd-Ep14InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      8EH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP14INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep14InDscrEnd:
 
Ep14OutDscr:
                db      Ep14OutDscrEnd-Ep14OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      0EH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP14OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep14OutDscrEnd:
 
Ep15InDscr:
                db      Ep15InDscrEnd-Ep15InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      8FH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP15INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep15InDscrEnd:
 
Ep15OutDscr:
                db      Ep15OutDscrEnd-Ep15OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      0FH             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP15OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep15OutDscrEnd:
$endif
$if (BULKINT)
Ep1001InDscr:
                db      Ep1001InDscrEnd-Ep1001InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      81H             ;; Endpoint number, and direction
                db      ET_INT         ;; Endpoint type
                db      EP1INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep1001InDscrEnd:
 
Ep1001OutDscr:
                db      Ep1001OutDscrEnd-Ep1001OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      01H             ;; Endpoint number, and direction
                db      ET_INT        ;; Endpoint type
                db      EP1OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep1001OutDscrEnd:
 
Ep1002InDscr:
                db      Ep1002InDscrEnd-Ep1002InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      82H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP2INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH             ;; Polling interval
Ep1002InDscrEnd:
 
Ep1002OutDscr:
                db      Ep1002OutDscrEnd-Ep1002OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      02H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP2OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH             ;; Polling interval
Ep1002OutDscrEnd:
 
Ep1003InDscr:
                db      Ep1003InDscrEnd-Ep1003InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      83H             ;; Endpoint number, and direction
                db      ET_BULK        ;; Endpoint type
                db      EP3INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H            ;; Polling interval
Ep1003InDscrEnd:
 
Ep1003OutDscr:
                db      Ep1003OutDscrEnd-Ep1003OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      03H             ;; Endpoint number, and direction
                db      ET_BULK       ;; Endpoint type
                db      EP3OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H            ;; Polling interval
Ep1003OutDscrEnd:
 
Ep1004InDscr:
                db      Ep1004InDscrEnd-Ep1004InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      84H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP4INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep1004InDscrEnd:
 
Ep1004OutDscr:
                db      Ep1004OutDscrEnd-Ep1004OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      04H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP4OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep1004OutDscrEnd:
$endif
 
$if ((TOPOLOGY) | (ISO))
Intrfc101Dscr:
                db      Intrfc101DscrEnd-Intrfc101Dscr                ;; Descriptor length
                db      DSCR_INTRFC     ;; Descriptor type
                db      0               ;; Zero-based index of this interface
                db      1               ;; Alternate setting
$if (TOPOLOGY)
                db      10              ;; Number of end points
$elseif (ISO)
                db      2               ;; Number of end points
$endif
                db      0ffH            ;; Interface class
                db      00H             ;; Interface sub class
                db      00H             ;; Interface sub sub class
                db      7               ;; Interface descriptor string index
Intrfc101DscrEnd:
 
$if (TOPOLOGY)
Ep1011InDscr:
                db      Ep1011InDscrEnd-Ep1011InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      81H             ;; Endpoint number, and direction
                db      ET_INT         ;; Endpoint type
                db      EP1INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep1011InDscrEnd:
 
Ep1011OutDscr:
                db      Ep1011OutDscrEnd-Ep1011OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      01H             ;; Endpoint number, and direction
                db      ET_INT        ;; Endpoint type
                db      EP1OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep1011OutDscrEnd:
 
Ep1012InDscr:
                db      Ep1012InDscrEnd-Ep1012InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      82H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP2INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH             ;; Polling interval
Ep1012InDscrEnd:
 
Ep1012OutDscr:
                db      Ep1012OutDscrEnd-Ep1012OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      02H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP2OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH             ;; Polling interval
Ep1012OutDscrEnd:
 
Ep1013InDscr:
                db      Ep1013InDscrEnd-Ep1013InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      83H             ;; Endpoint number, and direction
                db      ET_BULK        ;; Endpoint type
                db      EP3INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H            ;; Polling interval
Ep1013InDscrEnd:
 
Ep1013OutDscr:
                db      Ep1013OutDscrEnd-Ep1013OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      03H             ;; Endpoint number, and direction
                db      ET_BULK       ;; Endpoint type
                db      EP3OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H            ;; Polling interval
Ep1013OutDscrEnd:
 
Ep1014InDscr:
                db      Ep1014InDscrEnd-Ep1014InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      84H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP4INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep1014InDscrEnd:
 
Ep1014OutDscr:
                db      Ep1014OutDscrEnd-Ep1014OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      04H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP4OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep1014OutDscrEnd:
$endif
 
$if ((ISO) | (TOPOLOGY))
Ep1011023InDscr:
                db      Ep1011023InDscrEnd-Ep1011023InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      88H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
              db      EP1023OUTSZL ;; Maximun packet size (LSB)
              db      EP1023OUTSZH ;; Max packect size (MSB)
;;                db      32 ;; Maximun packet size (LSB)
;;                db      0 ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep1011023InDscrEnd:
 
Ep1011023OutDscr:
                db      Ep1011023OutDscrEnd-Ep1011023OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      08H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
              db      EP1023OUTSZL ;; Maximun packet size (LSB)
              db      EP1023OUTSZH ;; Max packect size (MSB)
;;                db      32 ;; Maximun packet size (LSB)
;;                db      0 ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep1011023OutDscrEnd:
$endif
 
$if (TOPOLOGY)
;;-----------------------------------------------------------------------------
;;Any one endpoint can only be on a single interface for a configuration
;;Since all 30 endpoints are on interface 0 alternate setting 0, then
;;interface 1 can have no endpoints
;;-----------------------------------------------------------------------------
Intrfc110Dscr:
                db      Intrfc110DscrEnd-Intrfc110Dscr                ;; Descriptor length
                db      DSCR_INTRFC     ;; Descriptor type
                db      1               ;; Zero-based index of this interface
                db      0               ;; Alternate setting
                db      0               ;; Number of end points
                db      0ffH            ;; Interface class
                db      00H             ;; Interface sub class
                db      00H             ;; Interface sub sub class
                db      0               ;; Interface descriptor string index
Intrfc110DscrEnd:
 
$endif
$endif
 
EndConfig1:
 
$if (TOPOLOGY)
Config2Dscr:
                db      Config2DscrEnd-Config2Dscr                ;; Descriptor length
                db      DSCR_CONFIG     ;; Descriptor type
                db      (EndConfig2-Config2Dscr) mod 256 ;; Config + End Points length (LSB)
                db      (EndConfig2-Config2Dscr) / 256   ;; Config + End Points length (MSB)
                db      3               ;; Number of interfaces
                db      2               ;; Configuration value
                db      5               ;; Configuration string index
                db      10100000b       ;; Attributes (b7 - buspwr, b6 - selfpwr, b5 - rwu)
                db      50              ;; Power requirement (div 2 ma)
Config2DscrEnd:
 
Intrfc200Dscr:
                db      Intrfc200DscrEnd-Intrfc200Dscr                ;; Descriptor length
                db      DSCR_INTRFC     ;; Descriptor type
                db      0               ;; Zero-based index of this interface
                db      0               ;; Alternate setting
                db      0               ;; Number of end points
                db      0ffH            ;; Interface class
                db      00H             ;; Interface sub class
                db      00H             ;; Interface sub sub class
                db      8               ;; Interface descriptor string index
Intrfc200DscrEnd:
 
Intrfc210Dscr:
                db      Intrfc210DscrEnd-Intrfc210Dscr                ;; Descriptor length
                db      DSCR_INTRFC     ;; Descriptor type
                db      1               ;; Zero-based index of this interface
                db      0               ;; Alternate setting
                db      1               ;; Number of end points
                db      0ffH            ;; Interface class
                db      00H             ;; Interface sub class
                db      00H             ;; Interface sub sub class
                db      9              ;; Interface descriptor string index
Intrfc210DscrEnd:
 
Ep2103InDscr:
                db      Ep2103InDscrEnd-Ep2103InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      83H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP3INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep2103InDscrEnd:
 
 
Intrfc220Dscr:
                db      Intrfc220DscrEnd-Intrfc220Dscr                ;; Descriptor length
                db      DSCR_INTRFC     ;; Descriptor type
                db      2               ;; Zero-based index of this interface
                db      0               ;; Alternate setting
                db      2               ;; Number of end points
                db      0ffH            ;; Interface class
                db      00H             ;; Interface sub class
                db      00H             ;; Interface sub sub class
                db      10              ;; Interface descriptor string index
Intrfc220DscrEnd:
 
Ep2205InDscr:
                db      Ep2205InDscrEnd-Ep2205InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      85H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP5INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep2205InDscrEnd:
 
Ep2205OutDscr:
                db      Ep2205OutDscrEnd-Ep2205OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      05H             ;; Endpoint number, and direction
                db      ET_INT          ;; Endpoint type
                db      EP5INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      0FFH            ;; Polling interval
Ep2205OutDscrEnd:
 
Intrfc221Dscr:
                db      Intrfc221DscrEnd-Intrfc221Dscr                ;; Descriptor length
                db      DSCR_INTRFC     ;; Descriptor type
                db      2               ;; Zero-based index of this interface
                db      1               ;; Alternate setting
                db      3               ;; Number of end points
                db      0ffH            ;; Interface class
                db      00H             ;; Interface sub class
                db      00H             ;; Interface sub sub class
                db      11              ;; Interface descriptor string index
Intrfc221DscrEnd:
 
Ep2218InDscr:
                db      Ep2218InDscrEnd-Ep2218InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      88H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP8INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep2218InDscrEnd:
 
Ep2218OutDscr:
                db      Ep2218OutDscrEnd-Ep2218OutDscr          ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      08H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP8OUTSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep2218OutDscrEnd:
 
Ep2219InDscr:
                db      Ep2219InDscrEnd-Ep2219InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      89H             ;; Endpoint number, and direction
                db      ET_ISO          ;; Endpoint type
                db      EP9INSZ         ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      01H             ;; Polling interval
Ep2219InDscrEnd:
 
Intrfc222Dscr:
                db      Intrfc222DscrEnd-Intrfc222Dscr                ;; Descriptor length
                db      DSCR_INTRFC     ;; Descriptor type
                db      2               ;; Zero-based index of this interface
                db      2               ;; Alternate setting
                db      4               ;; Number of end points
                db      0ffH            ;; Interface class
                db      00H             ;; Interface sub class
                db      00H             ;; Interface sub sub class
                db      12              ;; Interface descriptor string index
Intrfc222DscrEnd:
 
Ep2221InDscr:
                db      Ep2221InDscrEnd-Ep2221InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      81H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP1INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep2221InDscrEnd:
 
Ep2223InDscr:
                db      Ep2223InDscrEnd-Ep2223InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      83H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP3INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep2223InDscrEnd:
 
Ep2225InDscr:
                db      Ep2225InDscrEnd-Ep2225InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      85H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP5INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep2225InDscrEnd:
 
Ep2227InDscr:
                db      Ep2227InDscrEnd-Ep2227InDscr            ;; Descriptor length
                db      DSCR_ENDPNT     ;; Descriptor type
                db      87H             ;; Endpoint number, and direction
                db      ET_BULK         ;; Endpoint type
                db      EP7INSZ             ;; Maximun packet size (LSB)
                db      00H             ;; Max packect size (MSB)
                db      00H             ;; Polling interval
Ep2227InDscrEnd:
 
EndConfig2:
 
$endif
StringDscr:
 
StringDscr0:
                db      StringDscr0End-StringDscr0              ;; String descriptor length
                db      DSCR_STRING
                db      09H,04H
StringDscr0End:
 
StringDscr1:
                db      StringDscr1End-StringDscr1              ;; String descriptor length
                db      DSCR_STRING
                db      'M',00
                db      'a',00
                db      'n',00
                db      'u',00
                db      'f',00
                db      'a',00
                db      'c',00
                db      't',00
                db      'u',00
                db      'r',00
                db      'e',00
                db      'r',00
StringDscr1End:
 
StringDscr2:
                db      StringDscr2End-StringDscr2              ;; Descriptor length
                db      DSCR_STRING
                db      'J',00
                db      'S',00
                db      'R',00
                db      '8',00
                db      '0',00
                db      ' ',00
                db      'T',00
                db      'C',00
                db      'K',00
                db      ' ',00
                db      'D',00
                db      'e',00
                db      'v',00
                db      'i',00
                db      'c',00
                db      'e',00
$if (TOPOLOGY)
                db      ' ',00
                db      '1',00
$elseif (BULKINT)
                db      ' ',00
                db      '2',00
$else
                db      ' ',00
                db      '3',00
$endif
StringDscr2End:
 
StringDscr3:
                db      StringDscr3End-StringDscr3              ;; Descriptor length
                db      DSCR_STRING
                db      'S',00
                db      'N',00
                db      '1',00
                db      '2',00
                db      '3',00
                db      '4',00
                db      '5',00
                db      '6',00
StringDscr3End:
 
StringDscr4:
                db      StringDscr4End-StringDscr4              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      'o',00
                db      'n',00
                db      'f',00
                db      'i',00
                db      'g',00
                db      ' ',00
                db      '1',00
StringDscr4End:
 
StringDscr5:
                db      StringDscr5End-StringDscr5              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      'o',00
                db      'n',00
                db      'f',00
                db      'i',00
                db      'g',00
                db      ' ',00
                db      '2',00
StringDscr5End:
 
StringDscr6:
                db      StringDscr6End-StringDscr6              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      '1',00
                db      ' ',00
                db      'I',00
                db      '0',00
                db      ' ',00
                db      'A',00
                db      'S',00
                db      '0',00
StringDscr6End:
 
StringDscr7:
                db      StringDscr7End-StringDscr7              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      '1',00
                db      ' ',00
                db      'I',00
                db      '0',00
                db      ' ',00
                db      'A',00
                db      'S',00
                db      '1',00
StringDscr7End:
 
StringDscr8:
                db      StringDscr8End-StringDscr8              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      '2',00
                db      ' ',00
                db      'I',00
                db      '0',00
                db      ' ',00
                db      'A',00
                db      'S',00
                db      '0',00
StringDscr8End:
 
StringDscr9:
                db      StringDscr9End-StringDscr9              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      '2',00
                db      ' ',00
                db      'I',00
                db      '1',00
                db      ' ',00
                db      'A',00
                db      'S',00
                db      '0',00
StringDscr9End:
 
StringDscr10:
                db      StringDscr10End-StringDscr10              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      '2',00
                db      ' ',00
                db      'I',00
                db      '2',00
                db      ' ',00
                db      'A',00
                db      'S',00
                db      '0',00
StringDscr10End:
 
StringDscr11:
                db      StringDscr11End-StringDscr11              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      '2',00
                db      ' ',00
                db      'I',00
                db      '2',00
                db      ' ',00
                db      'A',00
                db      'S',00
                db      '1',00
StringDscr11End:
 
StringDscr12:
                db      StringDscr12End-StringDscr12              ;; Descriptor length
                db      DSCR_STRING
                db      'C',00
                db      '2',00
                db      ' ',00
                db      'I',00
                db      '2',00
                db      ' ',00
                db      'A',00
                db      'S',00
                db      '2',00
StringDscr12End:
 
StringDscr13:
                db      StringDscr13End-StringDscr13              ;; Descriptor length
                db      DSCR_STRING
                db      0DEH,030H
                db      0F3H,030H
                db      0A5H,030H
                db      '2',00
                db      ' ',00
                db      'I',00
                db      '2',00
                db      ' ',00
                db      'A',00
                db      'S',00
                db      '2',00
StringDscr13End:
 
UserDscr:
                dw      0000H
                end
 
