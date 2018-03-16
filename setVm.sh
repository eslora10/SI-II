tar -zxf si2srv.tgz
mv si2srv si2srv1

tar -zxf si2srv.tgz
mv si2srv si2srv2

cd si2srv1
./si2fixMAC.sh 2401 4 1
vmplayer ./si2srv.vmx &
cd ../si2srv2
./si2fixMAC.sh 2401 4 2
vmplayer ./si2srv.vmx &
