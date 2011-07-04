#!/bin/bash
echo "excise installer 0.0.1-pre (July 2, 2011)" >&2
echo "by Miorel-Lucian Palii" >&2
echo >&2

its="1309584384"

if ( type -p excise >& /dev/null ); then
	echo "It looks like you already have an excise command in your path variable." >&2
	echo "There's nothing more for me to do." >&2
	exit 1
fi

_dot_excise_exists_error () {
	echo "I won't be messing with it, but if you're certain it doesn't have to be there," >&2
	echo "move or remove it and run this script again." >&2
	exit 1
}

cd

if [[ -d .excise ]]; then
	echo "You already seem to have a .excise in your home directory. It could have been" >&2
	echo "created by another program or it may be the remnants of a previous install." >&2
	_dot_excise_exists_error
elif [[ -e .excise ]]; then
	echo "You already seem to have a .excise in your home directory. Was it created by" >&2
	echo "another program?" >&2
	_dot_excise_exists_error
fi

domain () {
	for cmd in domainname dnsdomainname nisdomainname ypdomainname; do
		if ( type "$cmd" >& /dev/null ); then
			domain="$("$cmd" 2> /dev/null | chomp)"
			if [[ "$domain" != "" && "$domain" != "(none)" && "$domain" != "$cmd: Local domain name not set" ]]; then
				echo -n "$domain"
				return 0
			fi
		fi
	done
	return 1
}

chomp () {
	perl -pe 's/[\r\n]//g'
}

_not_cise_warning () {
	echo "excise was specifically designed with CISE's quirks in mind." >&2
	echo "I can't recommend using it anywhere else with a clear conscience." >&2
	echo >&2
}

_cleanup_and_die () {
	echo "Installation failed, cleaning up." >&2
	cd
	rm -rf .excise
	exit 1
}

domain="$(domain)"
if (( $? )); then
	# couldn't determine domain name
	echo "I sure hope this is a CISE system... " >&2
	_not_cise_warning
elif [[ "$domain" != "cise.ufl.edu" ]]; then
	echo -n "Uh-oh, this doesn't seem to be a CISE system!" >&2
	_not_cise_warning
fi

for cmd in mkdir cp rm umask perl svn; do
	echo -n "Checking for $cmd... " >&2
	if ( ! type "$cmd" >& /dev/null ); then
		echo "no" >&2
		echo "Bailing :/" >&2
		exit 1
	fi
	echo "yes" >&2
done

umask 0077

fetch=""

if [[ "$fetch" == "" ]]; then
	echo -n "Checking for wget... " >&2
	if ( type wget >& /dev/null ); then
			fetch="wget -O"
			echo "yes" >&2
	else
		echo "no" >&2
	fi
fi

if [[ "$fetch" == "" ]]; then
	echo -n "Checking for curl... " >&2
	if ( type curl >& /dev/null ); then
		fetch="curl -f -o"
		echo "yes" >&2
	else
		echo "no" >&2
	fi
fi

if [[ "$fetch" == "" ]]; then
	echo -n "Checking for LWP::Simple... " >&2
	if ( perl -e 'use LWP::Simple' >& /dev/null ); then
			fetch="perl -MLWP::Simple -e open(\$o,\">\".shift)or(die);select\$o;is_success(getprint(shift))or(die);close\$o"
			echo "yes" >&2
	else
		echo "no" >&2
	fi
fi

if [[ "$fetch" == "" ]]; then
	echo "None of the web fetching mechanisms I know are available :(" >&2
	exit 1
fi

echo "Checking installer version..." >&2
latest_its="$($fetch - http://prmf.googlecode.com/svn/trunk/excise/1309571588.txt | perl -ne 'chomp;print if s/^latest_its\s+//')"
if [[ "$latest_its" == "" ]]; then
	echo >&2
	echo "I couldn't check if this is the latest version of the installation script." >&2
	echo "If this is due to a change in the site's layout, then it probably isn't." >&2
	echo "You should investigate at <http://prmf.googlecode.com/>." >&2
	exit 1
else
	if [[ "$latest_its" == "$its" ]]; then
		echo "This is the latest installer. Good." >&2
	elif [[ ${#latest_its} -gt ${#its} || ( ${#latest_its} -eq ${#its} && "$latest_its" -gt "$its" ) ]]; then
		echo >&2
		echo "A newer installer is available!" >&2
		echo "Get it from <http://prmf.googlecode.com/> and use that instead!" >&2
		exit 1
	else # its > latest_its
		echo >&2
		echo "Hmm, it seems like this script is newer than the one online! Hacking, are we?" >&2
		#exit 1
	fi
fi

mkdir .excise || exit 1
cd .excise
mkdir -p bin var/excise tmp || _cleanup_and_die 
echo "Downloading excise from SVN..." >&2
svn co http://prmf.googlecode.com/svn/trunk/excise/ tmp/excise >&2 || _cleanup_and_die
cp -f tmp/excise/bin/excise bin || _cleanup_and_die
cp -f tmp/excise/package-info.txt var/excise || _cleanup_and_die
chmod 0500 bin/excise || _cleanup_and_die
rm -rf tmp/excise

tcsh_file=""
bash_file=""
for shell in "$(basename $SHELL 2> /dev/null)" "$(ps -p "$PPID" -o comm= 2> /dev/null)"; do
	if [[ "$shell" == "tcsh" ]]; then
		tcsh_file=1
	elif [[ "$shell" == "bash" ]]; then
		bash_file=1
	fi
done

cd

if [[ "$tcsh_file" != "" ]]; then
	echo -n "Updating your .tcshrc file... " >&2
	tcsh_file=".tcshrc"
	if [[ -f "$tcsh_file" ]]; then
		echo >> "$tcsh_file" || tcsh_file=""
	fi
	if [[ "$tcsh_file" != "" ]]; then
		{
			echo "# Automatically added during installation of the excise tool:"
			echo "set path=(~/.excise/bin \$path)" 
		} >> "$tcsh_file" || tcsh_file=""
	fi
	if [[ "$tcsh_file" != "" ]]; then
		echo "OK" >&2
	else
		echo "Oops, there was an error. You'll have to do it yourself!" >&2
	fi
fi

if [[ "$bash_file" != "" ]]; then
	bash_file=".bashrc"
	if [[ -e .bash_profile ]]; then
		if [[ -e .bashrc ]]; then
			echo "You have both .bashrc and .bash_profile, I'm not sure which to use!" >&2
		else
			bash_file=".bash_profile"
		fi
	fi
	echo -n "Updating your $bash_file file... " >&2
	if [[ -f "$bash_file" ]]; then
		echo >> "$bash_file" || bash_file=""
	fi
	if [[ "$bash_file" != "" ]]; then
		{
			echo "# Automatically added during installation of the excise tool:"
			echo "export PATH=~/.excise/bin:\$PATH" 
		} >> "$bash_file" || bash_file=""
	fi
	if [[ "$bash_file" != "" ]]; then
		echo "OK" >&2
	else
		echo "Oops, there was an error. You'll have to do it yourself!" >&2
	fi
fi

rc=""
for file in "$tcsh_file" "$bash_file"; do
	[[ "$file" != "" ]] && rc="$rc and $file"
done
rc=${rc# and }

echo >&2
echo "excise tool successfully installed!" >&2
if [[ "$rc" == "" ]]; then
	echo "To use it, you'll have to update your path variable to include ~/.excise/bin." >&2
	echo "For tcsh, add \`set path=(~/.excise/bin \$path)' to .tcshrc." >&2
	echo "For bash, add \`export PATH=~/.excise/bin:\$PATH' to .bashrc." >&2
else
	echo "I've taken the liberty of updating the path in $rc for you." >&2
	echo "It is IMPERATIVE that you check the results are what you expect!" >&2
	echo "Afterwards, start a new shell or source the appropriate profile and enjoy." >&2
fi

echo >&2
echo "The installation script isn't needed anymore so it's safe to remove it." >&2
echo "Here's the command for your copy-pasting pleasure:" >&2
echo "# rm '$0'" >&2

exit 0
