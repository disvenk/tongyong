@echo off
SET JSFOLDER=D:\work\Chintec\svnRepository\Dev\sourceCode\trunk\RPWX\src\main\webapp\js\customSrc
SET BINFOLDER=D:\work\Chintec\svnRepository\Dev\sourceCode\trunk\RPWX\src\main\webapp\js\customBin
echo 正在查找JS文件
chdir /d %JSFOLDER%
for %%a in (*.js) do (
    @echo 正在压缩 %%~fa ...
  uglifyjs %%~fa  -m -o %BINFOLDER%\%%a
)
echo 完成!

pause & exit