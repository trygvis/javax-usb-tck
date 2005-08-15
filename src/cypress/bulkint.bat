@echo off
REM #--------------------------------------------------------------------------
REM #   File:           BULKINT.BAT
REM #   Contents:       Batch file to build firmware for JSR80--javax.usb TCK
REM #                   images.  This batch file calls abuild.bat with options
REM #                   required to create the images required for the bulk and
REM #                   interrupt tests.
REM #
REM #   Copyright (c) 2003 International Business Machines Corporation.
REM #     All rights reserved.
REM #--------------------------------------------------------------------------
REM Creates the B6 EEPROM image for the javax.usb TCK
REM BULK and INTERRUPT I/O TESTS.
REM Renumeration is possible with vendor request 0xA8
REM The name of the device is JSR TCK Device 2
REM The EEPROM will count by 1 if code is running correctly
abuild -x -b
