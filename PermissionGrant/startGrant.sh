#!/bin/bash


###choose application, two options, grant user and regrant 

if [ ! -d "logs" ]; then
	`mkdir logs`
fi

echo `date` >> logs/granting.log
echo `who am i` >> logs/granting.log 


echo "Please choose the application"
while true;do
        read -p "1:Grant New User 2:Regrant Permission " appoption
        case $appoption in
		#1|2) break;;
		1) application="Grant New User";break;;
		2) application="Regrant Permission";break;;
                * ) echo "please choose the application!";;
        esac;
done


if [ $appoption == 2 ]; then
	echo "I am in "$application
	echo "Please choose the environment"
	while true;do
       		read -p "1:DEV 2:SIT 3:UAT 4:PRD " option
        	case $option in
			1 ) env=DEV;break;;
			2 ) env=SIT;break;;
			3 ) env=UAT;break;;
			4 ) env=PRD;break;;
			* ) echo "please choose the environemnt!";;
        	esac;
	done





	read -p "Please input the schema: " schema
	read -p "Please input the table: " table

	if [ "$table" == "*" ]; then

        	table="'*'"
	fi

	echo $application",env="$env",schema="$schema",table="$table
	while true;do
        	read -p "Is the above information correct? [Yes|No]" option
        	case $option in
                	Yes ) break;;
                	No  ) echo "Please run the script again to correct your parameter!";exit;;

        	esac;
	done
	java -Denv=$env -jar GrantPermission-2.0-SNAPSHOT-jar-with-dependencies.jar $schema $table

elif [ $appoption == 1 ]; then

	echo "I am in grant user "

	####choose the environment
	echo "Please choose the environment"
	while true;do
        	read -p "1:DEV 2:SIT 3:UAT 4:PRD " option
        	case $option in
			1 ) env=DEV;break;;
			2 ) env=SIT;break;;
			3 ) env=UAT;break;;
			4 ) env=PRD;break;;
			* ) echo "please choose the environemnt!";;
        	esac;
	done




	read -p "Please input the user you want to grant permission: " user
	echo "Please select the group which user is in:"
	while true;do
		read -p "1:DBA 2:Deployment 3:ReadOnly 4:ReadWrite 5:ReadWriteEsecute " option
		case $option in 
			1 ) group=$env"_GEMFIRE_DBA_SG";break;;
			2 ) group=$env"_GEMFIRE_Deployment_SG";break;;
			3 ) group=$env"_GEMFIRE_ReadOnly_SG";break;;	
			4 ) group=$env"_GEMFIRE_RW_SG";break;;
			5 ) group=$env"_GEMFIRE_RWE_SG";break;;
			* ) echo "please choose the group!";;
		esac;
	done

	read -p "Please input the schema: " schema
#	if [[ $env == "DEV" ]] || [[ $env == "SIT" ]]; then
#		if [[ $group == *"_GEMFIRE_ReadOnly_SG" ]] || [[ $group == *"_GEMFIRE_RW_SG" ]]; then
#			read -p "Please input your team: " team
#			echo $applicaiton",user="$user", group="$group", schema="$schema" ,team="$team
#		else
#			team="ETL"
#			echo $application",user="$user", group="$group", schema="$schema
#		fi
#	else
#		#only ETL,DBA team can access schema MONITOR, the logic is in the java codes
#		if [ $schema == "MONIROT" ];then
#			team="ETL"
#		else
#			team="SERVICE"
#		fi
#		echo $application",user="$user", group="$group", schema="$schema
#	fi


	while true;do
		read -p "Is the above information correct? [Yes|No]" option
		case $option in
			Yes ) break;;
			No  ) echo "Please run the script again to correct your parameter!";exit;;	

		esac;
	done
	#table="'*'"
	java -Denv=$env -jar GrantPermission-2.0-SNAPSHOT-jar-with-dependencies.jar $user $group $schema 

fi
