searchTenant=$1
replaceTenant=$2
instanceName=$3

mv $T_WORK/emaas.properties.log $T_WORK/emaas.properties_tmp

#touch emaas.properties.log
echo "this is tenant update with user parameter $replaceTenant"
sed  "s/$searchTenant/$replaceTenant/" $T_WORK/emaas.properties_tmp > $T_WORK/emaas.properties.log
if [ -z  "$3" ] 
then 
	echo "there is no instance passed for this update"
else
	echo "update the instance name in property log file" 
 	sed -i '$ a INTSTANCE_NAME='"$3"  $T_WORK/emaas.properties.log
 fi 
rm $T_WORK/emaas.properties_tmp
