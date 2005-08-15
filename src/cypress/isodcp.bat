@echo off
REM #--------------------------------------------------------------------------
REM #   File:           ISODCP.BAT
REM #   Contents:       Batch file to build firmware for JSR80--javax.usb TCK
REM #                   images.  This batch file calls abuild.bat with options
REM #                   required to create the images required for the isochronous
REM #                   and default control pipe tests.
REM #
REM #   Copyright (c) 2003 International Business Machines Corporation.
REM #     All rights reserved.
REM #--------------------------------------------------------------------------
REM Creates the B6 EEPROM image for the javax.usb TCK
REM ISOCHRONOUS and DEFAULT CONTROL PIPE tests
REM The name of the device is JSR TCK Device 3
REM The EEPROM will count by 2 if code is running correctly
abuild -x -o
