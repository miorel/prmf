#!/usr/local/bin/perl
#
# ytradio - A synchronized, continuous playlist of YouTube videos
# Copyright (C) 2010 Miorel-Lucian Palii
# (see http://www.google.com/profiles/mlpalii for contact info)
#
# This project is free software: you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software
# Foundation, either version 3 of the License, or (at your option) any later
# version. See: http://www.gnu.org/licenses/gpl.html
#

use warnings;
use strict;

use CGI;

my $cgi = new CGI;
print $cgi->header;

if($cgi->request_method() eq "POST") {
	my @dur = (267.833, 380.347, 273.037, 180.76, 230.163, 211.042, 285.84, 222.56, 218.651, 205.596);
	my @id = ("U26ViV70940", "iTP6Z6ixjiA", "OG3PnQ3tgzY", "ENdWUrrnuDk", "Z8wuRITUHfw", "pxu6iQ28arw", "HI4kwzPsi0I", "cOrc37wNUqU", "Pyly3JtXoy4", "5AfTl5Vg73A");

	my $sum = 0;
	$sum += $_ for @dur;

	my $time = $^T - $sum * int($^T / $sum);

	for(my $i = 0;; ++$i) {
		if($time > $dur[$i]) {
			$time -= $dur[$i];
		}
		else {
			$time = int($time + .5);
			print "$id[$i] $time";
			last;
		}
	}
}
else {
	if(open my $fh, "<$0") {
		my @header = ();
		while(<$fh>) {
			chomp;
			last if !s/^\s*\#\s*//;
			push @header, $_ if !/^!/ && (length || @header);
		}
		close $fh;
		pop @header while @header && !length($header[-1]);
		print "<!--\n", (map {"\t$_\n"} @header), "-->\n\n" if @header;
	}
	print while <DATA>;
}

__DATA__
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>YouTube Radio</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/themes/black-tie/jquery-ui.css" type="text/css" />
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">/*<![CDATA[*/
	google.load("jquery", "1.4.2");
	google.load("swfobject", "2.1");
	google.load("jqueryui", "1.8.2");
/*]]>*/</script>
<script type="text/javascript">/*<![CDATA[*/
var ytp;
var vidPlaying;

function sync() {
	var xh = getXMLHttp();
	xh.onreadystatechange = function() {
		if(xh.readyState == 4 && xh.status == 200) {
			var response = xh.responseText.split(" ");
			var vid = response[0];
			var pos = response[1];
			if(ytp) {
				ytp.playVideo();
				if(vidPlaying == vid) {
					ytp.seekTo(pos, true);
				}
				else {
					ytp.loadVideoById(vid, pos);
				}
			}
			vidPlaying = vid;
		}
	}
	var params = "";
	xh.open("POST", window.location, true);
	xh.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xh.setRequestHeader("Content-length", params.length);
	xh.setRequestHeader("Connection", "close");
	xh.send(params);
}

function updatePos() {
	if(ytp) {
		var current = ytp.getCurrentTime();
		var total = ytp.getDuration();
		$("#pos").html(current >= 0 && total >= 0 ? secsToMins(current) + " / " + secsToMins(Math.round(total)) : "-:-- / -:--");
	}
}

function secsToMins(s) {
	s = Math.floor(s);
	var m = Math.floor(s / 60);
	s = s % 60;
	return m + ":" + (s < 10 ? "0" : "") + s;
}
    
function onPlayerError(code) {
	$("#err").html("Error! Code = " + code);
}

function onPlayerStateChange(state) {
	if(ytp) {
		if(vidPlaying) {
			$("#id").html(vidPlaying);
			$("#quality").html(ytp.getPlaybackQuality());
			$("#duration").html(ytp.getDuration());
			$("#state").html(ytp.getPlayerState());
			$("#vol").html(ytp.getVolume());
			$("#info").show();
		}

		$("#volume").slider("value", ytp.getVolume());
		switch(state) {
		case 0:
			sync();
			break;
		case 1:
			$("#pause").button({label: "Pause"});
			break;
		case 2:
		case 5:
			$("#pause").button({label: "Play"});
			break;
		}
	}
}

function getXMLHttp() {
	return window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
}

function onYouTubePlayerReady(playerId) {
	ytp = $("#ytp")[0];

	ytp.addEventListener("onError", "onPlayerError");
	ytp.addEventListener("onStateChange", "onPlayerStateChange");

	$("#controls").show();
	var pause = $("#pause");
	pause.css({width: pause.outerWidth()});

	setInterval(updatePos, 250);

	sync();
}

$(function() {
	$("#vid").html("Loading...");
	$("#controls > *").css({"margin-top": 10, "margin-bottom": 10});

	var params = {allowScriptAccess: "always"};
	var atts = {id: "ytp"};
	swfobject.embedSWF("http://www.youtube.com/apiplayer?enablejsapi=1&autoplay=1&rel=0&disablekb=1&showsearch=0&iv_load_policy=3&playerapiid=player1", "vid", "640", "385", "8", null, null, params, atts);

	$("#sync").button({label: "Synchronize"}).click(function() { sync(); });
	$("#mute").button({label: "Mute"})
		.click(function() {
			if(ytp) {
				if($(this)[0].checked) {
					ytp.mute();
				}
				else {
					ytp.unMute();
				}
			}
		}
	);
	$("#pause").button({label: "Pause"})
		.click(function() {
			if(ytp) {
				if($(this).button("option", "label") == "Pause") {
					ytp.pauseVideo();
				}
				else {
					ytp.playVideo();
				}
			}
		}
	);
	$("#volume").slider({range: "min", min: 0, max: 100, value: 0, orientation: "horizontal", animate: "true",
		slide: function(event, ui) {
			if(ytp) {
				ytp.setVolume(ui.value);
				$("#vol").html(ytp.getVolume());
			}
		}
	});
});
/*]]>*/</script>
</head>

<body style="text-align:center;">
	<div id="vid">This page requires JavaScript to function properly.</div>
	<div id="controls" style="display:none;">
		<div>
			<input type="submit" id="sync" />
			<input type="checkbox" id="mute" /><label for="mute"></label>
			<input type="submit" id="pause" />
		</div>
		<div id="volume" style="width:40%;left:30%"></div>
	</div>
	<div id="info" style="display:none;">
		Video ID: <span id="id"></span><br />
		Quality: <span id="quality"></span><br />
		Duration: <span id="duration"></span><br />
		Player state: <span id="state"></span><br />
		Volume: <span id="vol"></span><br />
		Current position: <span id="pos"></span><br />
	</div>
</body>
</html>
