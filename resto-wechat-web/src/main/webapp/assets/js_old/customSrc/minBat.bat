@echo off
SET JSFOLDER=D:\work\Chintec\svnRepository\Dev\sourceCode\trunk\RPWX\src\main\webapp\js\customSrc
SET BINFOLDER=D:\work\Chintec\svnRepository\Dev\sourceCode\trunk\RPWX\src\main\webapp\js\customBin
echo ���ڲ���JS�ļ�
chdir /d %JSFOLDER%
for %%a in (*.js) do (
    @echo ����ѹ�� %%~fa ...
  uglifyjs %%~fa  -m -o %BINFOLDER%\%%a
)
echo ���!

pause & exit