@echo off
REM #--------------------------------------------------------------------------
REM #   File:           TOPOLOGY.BAT
REM #   Contents:       Batch file to build firmware for JSR80--javax.usb TCK
REM #                   images.  This batch file calls abuild.bat with options
REM #                   required to create the images required for the topology test.
REM #
REM #   Copyright (c) 2003 International Business Machines Corporation.
REM #     All rights reserved.
REM #--------------------------------------------------------------------------
REM Creates the B6 EEPROM image for the javax.usb TCK
REM TOPOLOGY TEST.
REM
REM This device has no functionality other than what is in
REM the base frameworks.
REM
REM The product id for this device is set so that it is not
REM recognized by the ezusb.sys driver.  This is done because
REM some of the descriptors defined in DSCR.A51 are not supported
REM by the sample driver.
REM When the device is reset on Windows, the new device wizard will
REM start.  When this happens, click cancel to dismiss the wizard.
REM
REM With this product id, the EZ-USB Control Panel application will
REM not communicate with the development board.
REM If you want to reprogram the EEPROM after it has been programmed
REM with the topology test image, you will need to do the following:
REM
REM Remove the "EEPROM CONNECT" jumper from the EZ-USB Development Board
REM Plug the USB cable from the development board to the computer.
REM Replace the "EEPROM CONNECT" jumper.
REM Select the Download button on the EZ-USB Control Panel and select
REM    the Vend-Ax.hex file which was installed with the development kit.
REM Setup a Vendor Request, by filling in the fields on the "Vend Req"
REM    button line as follows:  Req=0xA9; Value=0x0; Index=0xBEEF; Length=1;
REM    Dir=0 OUT; Hex Bytes=FF.
REM Select the Vend Req button which will program the first byte of the EEPROM
REM    to 0xFF which is an unprogrammed EEPROM.
REM (Opt:  You can verify that the first byte was programmed with 0xFF by
REM    changing setting Dir=1 IN and seeing the byte returned is 0xFF)
REM Press the reset button on the development board or replug the USB cable
REM    and the device is now recognized as the built in EZ-USB FX Device.
REM This device is recognized by the general purpose driver and the
REM    EZ-USB Control Panel application will find the device.
REM To program the EEPROM, select the EEPROM button on the control panel.
REM    Select the EEPROM image to program into the EEPROM.
REM
REM Refer to the documentation in the EZ-USB FX Development Kit (CY3671) for
REM    more information.
REM
REM The name of the device is JSR TCK Device 1
REM The display will not count when programmed for this test.
abuild -x -t
