#
# bigstick - A tool for better bash-ing!
# Copyright (C) 20011 Miorel-Lucian Palii <mlpalii@gmail.com>
#
# This library is free software: you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software
# Foundation, either version 3 of the License, or (at your option) any later
# version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
# FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
# details.
#
# You should have received a copy of the GNU General Public License along with
# this program. If not, see <http://www.gnu.org/licenses/>.
#

SED_WHITESPACE_REGEX='[ \f\n\r\t\v]'

# filter
# strips return and newline characters from input
chomp () {
	tr -d '\r\n'
}

# filter
# removes whitespace at the beginning and end of each line in the input
trim () {
	sed "s/^$SED_WHITESPACE_REGEX\\+//; s/$SED_WHITESPACE_REGEX\\+\$//"
}

# filter
# collapses multiple whitespace characters to a single space; whitespace at the beginning and end of the line is removed completely
collapse () {
	trim | sed "s/$SED_WHITESPACE_REGEX\\+/ /g"
}

# filter
# echoes the lines of input that contain at least one non-whitespace character
strip_empties () {
	# number of fields is non-zero
	awk NF
}

# filter
strip_comment_lines () {
	grep -vE '^[[:space:]]*#'
}

# filter
# prints number of lines in input
line_count () {
	# `wc -l` counts newlines, so it will be off by one if the last line doesn't have a newline

	# set output record separator to empty string, print number of records at end
	awk 'END {ORS = ""; print NR}'
}

# filter
# prints the first line of input
first_line () {
	head -n1
}

# filter
# prints the last line of input
last_line () {
	tail -n1
}

# filter
# prints the first word of every line in the input
# empty lines stay empty
first_word () {
	# print field in position 1
	awk '{print $1}'
}

# filter
# prints the last word of every line in the input
# empty lines stay empty
last_word () {
	# print field in position "number of fields"
	awk '{print $NF}'
}
