function HCB() {
}

HCB.config = {};

HCB.save = function() {
	var txt = $("#txtDir").val();

	txt = txt.replace(/\\/g, "/");

	if (! txt.endsWith("/"))
		txt = txt + "/";

	$("#txtDir").val(txt);
	HCB.config["BACKUP_DIR"] = txt;

	txt = $("#txtZipDir").val();
	
	txt = txt.replace(/\\/g, "/");

	if (! txt.endsWith("/"))
		txt = txt + "/";

	$("#txtZipDir").val(txt);
	HCB.config["DIR_ZIP"] = txt;

	var jData = HCB.config;

	$.post("/create", JSON.stringify(jData))
    .done(function(data) {
    	HCB.alert(data["MSG"]);
    });
}

HCB.validate = function() {
	var list = $('.isrequired');

	for (var i = 0; i < list.length; i++) {
    	var widget = $(list[i]);

    	if (widget.val() == "") {
    		HCB.alert(widget.attr("cfgmsg"));
      		return false;
    	}
    };

    HCB.config["BACKUP_DIR"] = $("#txtDir").val();
    HCB.config["NOTIFICATION"] = $("#txtNotification").val();
    HCB.config["CFG_FILE"] = $("#txtCfgName").val();

    if ($("#chkPurge").prop("checked"))
    	HCB.config["PURGE"] = "1";
    else
    	HCB.config["PURGE"] = "0";

    return true;
}

HCB.validateDB = function() {
	delete HCB.config["DIR_ZIP"];
	delete HCB.config["ZIP_NAME"];

	delete HCB.config["DB_HOSTNAME"];
    delete HCB.config["DB_NAME"];
    delete HCB.config["DB_USER"];
    delete HCB.config["DB_PASSWORD"];
    delete HCB.config["DB_PORT"];

    delete HCB.config["SQL_DIFFERENTIAL"];
	delete HCB.config["SQL_COMPRESSION"];
    delete HCB.config["ORACLE_SID"];
    delete HCB.config["ORACLE_DUMPDIR"];
    delete HCB.config["ORACLE_COMPRESSION"];

    delete HCB.config["PARALLELISM"];

    var bt = $("#cmbType").val();

	HCB.config["BACKUP_TYPE"] = $("#cmbType").val();

	// Is zip
	if (bt == "1") {
		if ($("#txtZipDir").val() == "") {
			HCB.alert("Backup directory to zip is required");
			return false;
		}

		if ($("#txtZipName").val() == "") {
			HCB.alert("Zip name is required");
			return false;
		}

		HCB.config["DIR_ZIP"] = $("#txtZipDir").val();
		HCB.config["ZIP_NAME"] = $("#txtZipName").val();
	}
	else {
		var list = $('.requireddb');
	
		for (var i = 0; i < list.length; i++) {
	    	var widget = $(list[i]);

	    	if (widget.val() == "") {
	    		HCB.alert(widget.attr("cfgmsg"));
	      		return false;
	    	}
	    };

	    HCB.config["DB_HOSTNAME"] = $("#txtHostname").val();
	    HCB.config["DB_NAME"] = $("#txtDbName").val();
	    HCB.config["DB_USER"] = $("#txtUserName").val();
	    HCB.config["DB_PASSWORD"] = $("#txtDbPassword").val();
	    HCB.config["DB_PORT"] = $("#txtDbPort").val();

	    // MySQL Pump
	    if (bt == "3") {
	    	if ($("#txtPara").val() == "") {
				HCB.alert("Default parallelism is required");
				return false;
			}

	    	HCB.config["PARALLELISM"] = $("#txtPara").val();
	    }

	    // SQL Server
	    if (bt == "4") {
	    	if ($("#chkSQLDiff").prop("checked"))
		    	HCB.config["SQL_DIFFERENTIAL"] = "1";
		    else
		    	HCB.config["SQL_DIFFERENTIAL"] = "0";

		    if ($("#chkSQLComp").prop("checked"))
		    	HCB.config["SQL_COMPRESSION"] = "1";
		    else
		    	HCB.config["SQL_COMPRESSION"] = "0";
	    }

	    // Is Oracle
	    if (bt == "6") {
	    	if ($("#txtOraSID").val() == "") {
				HCB.alert("Oracle SID is required");
				return false;
			}

			if ($("#txtOraDump").val() == "") {
				HCB.alert("Oracle dump directory is required");
				return false;
			}

			HCB.config["ORACLE_SID"] = $("#txtOraSID").val();
			HCB.config["ORACLE_DUMPDIR"] = $("#txtOraDump").val();

			if ($("#chkOraComp").prop("checked"))
		    	HCB.config["ORACLE_COMPRESSION"] = "1";
		    else
		    	HCB.config["ORACLE_COMPRESSION"] = "0";
	    }
	}

    return true;
}

HCB.validateStorage = function() {
	delete HCB.config["AWS_KEY"];
	delete HCB.config["AWS_SECRET"];
	delete HCB.config["S3_BUCKET"];
	delete HCB.config["S3_PATH"];

	delete HCB.config["AZBLOB_URL"];
	delete HCB.config["AZBLOB_CONTAINER"];
	delete HCB.config["AZBLOB_PATH"];

	delete HCB.config["GCS_AUTHFILE"];
	delete HCB.config["GCS_PROJECT"];
	delete HCB.config["GCS_BUCKET"];
	delete HCB.config["GCS_PATH"];

	var ss = $("#cmbStorage").val();

	HCB.config["STORAGE_TYPE"] = $("#cmbStorage").val();
	
	// AWS S3
	if (ss == "1") {
		if ($("#txtS3Key").val() == "") {
			HCB.alert("AWS S3 key is required");
			return false;
		}

		if ($("#txtS3Secret").val() == "") {
			HCB.alert("AWS S3 secret is required");
			return false;
		}

		if ($("#txtS3Bucket").val() == "") {
			HCB.alert("AWS S3 bucket is required");
			return false;
		}

		HCB.config["AWS_KEY"] = $("#txtS3Key").val();
		HCB.config["AWS_SECRET"] = $("#txtS3Secret").val();
		HCB.config["S3_BUCKET"] = $("#txtS3Bucket").val();
		HCB.config["S3_PATH"] = $("#txtS3Path").val();
	}

	// Azure Blob
	if (ss == "2") {
		if ($("#txtAzUrl").val() == "") {
			HCB.alert("Azure Blob URL is required");
			return false;
		}

		if ($("#txtAzCnt").val() == "") {
			HCB.alert("Azure Blob container is required");
			return false;
		}

		HCB.config["AZBLOB_URL"] = $("#txtAzUrl").val();
		HCB.config["AZBLOB_CONTAINER"] = $("#txtAzCnt").val();
		HCB.config["AZBLOB_PATH"] = $("#txtAzPath").val();
	}

	// Google Cloud Storage
	if (ss == "3") {
		if ($("#txtGCSAuth").val() == "") {
			HCB.alert("Google credentials file is required");
			return false;
		}

		if ($("#txtGCSProject").val() == "") {
			HCB.alert("Google project is required");
			return false;
		}

		if ($("#txtGCSBucket").val() == "") {
			HCB.alert("Google Cloud Storage bucket is required");
			return false;
		}

		HCB.config["GCS_AUTHFILE"] = $("#txtGCSAuth").val();
		HCB.config["GCS_PROJECT"] = $("#txtGCSProject").val();
		HCB.config["GCS_BUCKET"] = $("#txtGCSBucket").val();
		HCB.config["GCS_PATH"] = $("#txtGCSPath").val();
	}

	return true;
}

HCB.alert = function(msg) {
	$('#lblMsg').html(msg);
	$('#window-alert').modal('toggle');
}

HCB.showStorage = function() {
	var ss = $("#cmbStorage").val();

	switch (ss) {
		case "1":
			$("#divAwsS3").prop("hidden", false);
			$("#divAzure").prop("hidden", true);
			$("#divGCS").prop("hidden", true);
			break;

	    case "2":
	    	$("#divAwsS3").prop("hidden", true);
	    	$("#divAzure").prop("hidden", false);
	    	$("#divGCS").prop("hidden", true);
	    	break;

	    case "3":
	    	$("#divAwsS3").prop("hidden", true);
	    	$("#divAzure").prop("hidden", true);
	    	$("#divGCS").prop("hidden", false);
	    	break;
	}
}

HCB.showBackupType = function() {
	$("#divZip").prop("hidden", true);
	$("#divDB").prop("hidden", true);

	$("#divSQLServer").prop("hidden", true);
	$("#divOracle").prop("hidden", true);
	$("#divMySQL").prop("hidden", true);

	var bt = $("#cmbType").val();

	switch (bt) {
	    case "1":
	    	$("#divZip").prop("hidden", false);
	    	break;

	    case "2":
	    	$("#divDB").prop("hidden", false);
	    	$("#txtDbPort").val("3306");
	    	break;

	    case "3":
	    	$("#divDB").prop("hidden", false);
	    	$("#divMySQL").prop("hidden", false);
	    	$("#txtDbPort").val("3306");
	    	$("#txtPara").val("4");
	    	break;

	    case "4":
	    	$("#divDB").prop("hidden", false);
	    	$("#divSQLServer").prop("hidden", false);
	    	$("#txtDbPort").val("1433");
	    	break;

	    case "5":
	    	$("#divDB").prop("hidden", false);
	    	$("#txtDbPort").val("3306");
	    	$("#txtHostname").val("localhost");
	    	break;

	    case "6":
	    	$("#divDB").prop("hidden", false);
	    	$("#divOracle").prop("hidden", false);
	    	$("#txtDbPort").val("1521");
	    	break;

	    case "7":
	    	$("#divDB").prop("hidden", false);
			$("#txtDbPort").val("5432");
			break;

		case "8":
	    	$("#divDB").prop("hidden", false);
			$("#txtDbPort").val("27017");
			break;

		case "9":
	    	$("#divDB").prop("hidden", false);
	    	$("#txtDbPort").val("3306");
	    	$("#txtHostname").val("localhost");
	    	break;
  	}
}