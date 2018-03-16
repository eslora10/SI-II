# Ddownloads and set apache-jmeter
#wget http://ftp.cixug.es/apache//jmeter/binaries/apache-jmeter-4.0.tgz
#tar -xzvf apache-jmeter-4.0.tgz

# Set both vm 1 and 2 
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
