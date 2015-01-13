/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function findHTMLElmt( TAG, obj ) {
        var temp = obj.parentNode;
        while( temp.tagName.toUpperCase() != TAG )
                temp = temp.parentNode;
        return temp;
}


function findFrame( parentFrame, frameName ) {
	for( var fi = 0; fi < parentFrame.length; fi ++ ) {
		if( parentFrame.frames[ fi ].name == frameName ) {
			return parentFrame.frames[ fi ];
		}
	}
	return null;
}
/*
function findSubHTMLElmt( TAG, obj ) {
	var temp = obj;
	while( obj.tagName.toUpperCase() != TAG && obj.hasChildNodes() ) {

	}
}*/
function getUniqueId() {
	var d = new Date();
	var Y = d.getFullYear();
	var M = d.getMonth()+1;
	var D = d.getDate();
	var H = d.getHours();
	var Min = d.getMinutes();
	var S = d.getSeconds();
	var MS = d.getMilliseconds();
	var Result = "" + Y;
	Result += (M>=10)?M:("0"+M );
	Result += (D>=10)?D:("0"+D );
	Result += (H>=10)?H:("0"+H );
	Result += (Min>=10)?Min:("0"+Min );
	Result += (S>=10)?S:("0"+S );
	Result += MS;
	while( Result.length < 24 ) {
			Result += Math.floor( Math.random()*10);
	}
	return Result;
}

function clearChild( obj ) {
	while( obj.hasChildNodes() ) {
		obj.removeChild( obj.lastChild );
	}
}

function checkUnSave( evtObj ) {
	if( unSaved ) {
		if( self.document.all ) {
			event.returnValue = Resource_DataUnsaved_IE;
		}
		else {
			evtObj.returnValue = Resource_DataUnsaved_FF;
		}
	}
}
function filterChar( str ) {
	str = str.replace(/'/g,"‘");
	str = str.replace(/\"/g, "“");
	str = str.replace(/@/g, "◎");
	//str = str.replace(/-/g, "－");
	str = str.replace(/,/g, "，");
	str = str.replace(/;/g, "；");
	str = str.replace(/&/g, "＆");
	str = str.replace(/</g, "＜");
	str = str.replace(/>/g, "＞");
	str = str.replace(/\\/g, "／" );
	return str;
}

function unfilterChar( str ) {
	str = str.replace(/◎/g, "@");
	str = str.replace(/－/g, "-");
	str = str.replace(/＃/g, "#");
	return str;
}

function filterForHTML( str ) {
	str = str.replace(/&/g,"&amp;");
	str = str.replace(/ /g,"&nbsp;");
	str = str.replace(/</g,"&lt;");
	str = str.replace(/>/g,"&gt;");
	str = str.replaceAll(/\r\n/g,"<BR>");
	str = str.replaceAll(/\n/g,"<BR>");
	return str;
}

/** 用于组合数据使用的预转码，在组合各个值之前使用，用于过滤各种可能的关键数据字符，包括 空格 回车 换行 < > ' " & # , . - = * ! | ` : / \ + _ */
function encodingData( str ) {
	str = str.replace(/&/g,"&amp;");
	str = str.replace(/ /g,"&nbsp;");
	str = str.replace(/</g,"&lt;");
	str = str.replace(/>/g,"&gt;");
	str = str.replace(/\r\n/g,"<BR>");
	str = str.replace(/\n/g,"<BR>");
	str = str.replace(/\r/g,"");
	str = str.replace(/\./g,"&dot;");
	str = str.replace(/,/g,"&comma;");
	str = str.replace(/\#/g,"&sharp;");
	str = str.replace(/\*/g,"&star;");		
	str = str.replace(/-/g,"&minor;");
	str = str.replace(/\+/g,"&plus;");
	str = str.replace(/_/g,"&underline;");
	str = str.replace(/=/g,"&equal;");
	str = str.replace(/'/g,"&singleCap;");
	str = str.replace(/"/g,"&cap;");
	str = str.replace(/!/g,"&stress;");
	str = str.replace(/\|/g,"&spliter;");        
	str = str.replace(/:/g,"&is;");
	str = str.replace(/\\/g,"&slash;");
	str = str.replace(/\//g,"&Uslash;");        
	str = str.replace(/`/g,"&tip;");
	return str;
}

function decodingData( str ) {
	str = str.replace(/&amp;/g,"&");
	str = str.replace(/&nbsp;/g," ");
	str = str.replace(/&lt;/g,"<");
	str = str.replace(/&gt;/g,">");
	str = str.replace(/<BR>/g,"\r\n");
	str = str.replace(/&dot;/g,".");
	str = str.replace(/&comma;/g,",");
	str = str.replace(/&sharp;/g,"#");
	str = str.replace(/&star;/g,"*");		
	str = str.replace(/&minor;/g,"-");
	str = str.replace(/&plus;/g,"+");
	str = str.replace(/&underline;/g,"_");
	str = str.replace(/&equal;/g,"=");
	str = str.replace(/&singleCap;/g,"\'");
	str = str.replace(/&cap;/g,"\"");
	str = str.replace(/&stress;/g,"!");
	str = str.replace(/&spliter;/g,"|");        
	str = str.replace(/&is;/g,":");
	str = str.replace(/&slash;/g,"\\");
	str = str.replace(/&Uslash;/g,"/");        
	str = str.replace(/&tip;/g,"`");
	return str;
}
//Get a css rule
function findCSSRule( selectorText ) {
	var cssShts = self.document.styleSheets;
	for( var i = 0; i < cssShts.length; i++ ) {
		var rules = ( cssShts[i].rules )?cssShts[i].rules:cssShts[i].cssRules;
		for( var j = 0; j < rules.length; j++ ) {
			if( rules[j].selectorText.toUpperCase() == selectorText.toUpperCase() ) return rules[j];
		}
	}
	return null;
}

function trim( Str ) {
	return Str.replace(/^\s+|\s+$/g,"");
}
	function repeat( S, N ) {
		var X = "";
		for( var i = 0; i < N ; i++ ){
			X += S;
		}
		return X;
	}
   function getAlignedNumber( N, length ) {
	   if( N == null ) return null;
	   var mm = N.match(/(\d+)(\.\d+)*/);
	   var ExactN ;
	   if( mm != null && mm.length > 0 ) {
		   ExactN = mm[0];
	   }
	   else {
		   return "";
	   }
	   var Ns = ExactN.split(/\./);
	   for( var i = 0; i < Ns.length; i++ ){
		   if( length > Ns[i].length ) {
			   Ns[i] = repeat( "0", length - Ns[i].length ) + Ns[i];
		   }
	   }
	   return Ns.join(".");
    }
	
	function getAlignedNumberString( N, length ) {
		if( N == null ) return null;
		var AN = getAlignedNumber( N, length );
		return N.replace( /(\d+)(\.\d+)*/, AN );
	}
        
        function myEncodeURI( S ) {
            return encodeURI( S ).replace(/'/g,"%27").replace(/&/g,"%26");
        }
        function myDecodeURI( S ) {
            return decodeURI( S ).replace(/%27/g,"'").replace(/%26/g,"&");
        }
        
