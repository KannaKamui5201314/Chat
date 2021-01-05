echo on
call protoc --version

call protoc --java_out=..\src\ cs_login.proto
call protoc --java_out=..\src\ cs_enum.proto

PAUSE