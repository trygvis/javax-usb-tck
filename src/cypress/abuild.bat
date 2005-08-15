@echo off
REM #--------------------------------------------------------------------------
REM #   File:           ABUILD.BAT
REM #   Contents:       Batch file to build firmware for JSR80--javax.usb TCK
REM #                   images.
REM #
REM #   Copyright (c) 2003 International Business Machines Corporation.
REM #     All rights reserved.
REM #--------------------------------------------------------------------------
 
REM You may need to modify the location of the Keil tools
REM Full version of the Keil development tools is required
SET PATH=%PATH%;c:\keil\c51\bin
SET C51INC=C:\keil\c51\inc
SET C51LIB=C:\KEIL\C51\lib
 
REM  command line switches
REM  ---------------------
REM  -erase Remove temporary files created during build process.
REM  -x     Locate code in internal RAM and build EEPROM images.  This
REM            requires the full version of Keil Tools
REM  -t     Create descriptor table for JSR80 TCK Toplogy Test
REM  -b     Create descriptor table for JSR80 TCK BULK or INTERRUPT Test
REM  -o     Create descriptor table for JSR80 TCK ISOCHRONOUS or DCP Test
 
REM Select name of periph.c replacement
REM SET PERIPH_C=
if "%2" == "-t" SET PERIPH=periph
if "%2" == "-b" SET PERIPH=bulkint
if "%2" == "-o" SET PERIPH=isodcp
 
REM Select name of hex and b6 file
REM SET OUTPUT_FN=
if "%2" == "-t" SET OUTPUT_FN=topology
if "%2" == "-b" SET OUTPUT_FN=bulkint
if "%2" == "-o" SET OUTPUT_FN=isodcp
 
REM Select location of XDATA
REM For the BULKINT tests, ISODISAB=1 so XDATA can go at 0x2000H; otherwise, XDATA needs to end
REM before 0x1B3F (or 0x1F40 if Bulk EPs not used, see Fig 3-3 of EZUSB FX Tech Ref)
if "%2" == "-b" SET XDATALOC=2000h
if not "%2" == "-b" SET XDATALOC=1400h
 
REM Compile Cypress FrameWorks code
c51 fw.c debug objectextend code small moddp2 OPTIMIZE(9,SIZE)
 
REM Compile user-modified periph.c code
REM Note: This code does not generate interrupt vectors
c51 %PERIPH%.c db oe code small moddp2 noiv OPTIMIZE(9,SIZE)
 
REM Assemble the descriptor table
if "%2" == "-t" a51 dscr.a51 errorprint debug SET(TOPOLOGY) RESET(BULKINT, ISO) XREF
if "%2" == "-b" a51 dscr.a51 errorprint debug SET(BULKINT) RESET(TOPOLOGY,ISO) NOCOND XREF
if "%2" == "-o" a51 dscr.a51 errorprint debug SET(ISO) RESET(TOPOLOGY, BULKINT) NOCOND XREF
 
REM Link object code (includes debug info)
REM Note: XDATA and CODE must not overlap
REM Note: using a response file here for line longer than 128 chars
echo fw.obj, dscr.obj, %PERIPH%.obj, > bld.rsp
echo support\USBJmpTb.obj,support\EZUSB.lib  >> bld.rsp
if "%1" == "-x" echo TO %OUTPUT_FN% RAMSIZE(256) PL(68) PW(78) CODE(80h) XDATA(%XDATALOC%)  >> bld.rsp
if not "%1" == "-x" echo TO %OUTPUT_FN% RAMSIZE(256) PL(68) PW(78) CODE(4000h) XDATA(6000h)  >> bld.rsp
bl51 @bld.rsp
 
REM Generate the intel hex image of binary - includes no debug info
oh51 %OUTPUT_FN% HEXFILE(%OUTPUT_FN%.hex)
 
REM Generate serial eeprom image for B2 (EZ-USB) bootload
if "%1" == "-x" support\hex2bix -i -o %OUTPUT_FN%.iic %OUTPUT_FN%.hex
 
REM Generate serial eeprom image for B6 (EZ-USB FX) bootload
if "%1" == "-x" support\hex2bix -i -r -m 8000 -f 0xB6 -o %OUTPUT_FN%.b6 %OUTPUT_FN%.hex
 
REM "-erase" used to remove temporary files after build completes
if "%1" == "-erase" del bld.rsp
if "%1" == "-erase" del *.lst
if "%1" == "-erase" del *.obj
if "%1" == "-erase" del *.m51
 
