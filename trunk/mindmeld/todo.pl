print MindMeld->header;

my $cgi = MindMeld->cgi;

print $cgi->p("This is my public brainstorming page for improvements to the site. If there's something you'd like to see here, give me a holler.");
print $cgi->p("<ul>" . join('', map {"<li>$_</li>"} grep {/\S/} qq^

create site icon

sanitize/escape all user input

redirect page STDERR and other goodies to application-specific file

allow deletion of questions

make grade setting more intuitive

allow creation of questions and categories directly on the site (currently only way is by upload of Mnemosyne XML)

allow editing of categories

add visibility attribute to questions and categories (public vs. private)

remove ensure_schema code from MindMeld.pm 

consolidate study.pl and set_grade.pl into one file (and save a redirect in the process)

improve post-login redirects, namely which variables get transferred through the query string

make it more obvious for the user if a session times out

find a name for the root user that's more clever than "admin"

take care of database integrity when objects are deleted

fix odd/even nth-child() quirk on Internet Explorer (i.e. change to CSS classes)

allow exporting questions as Mnemosyne XML (and maybe other formats)

rename categories to collections, allow a question to be part of more than one collection

make site completely functional when cookies are disabled

make site completely functional when JavaScript is disabled

make statistics page more exciting

create SVG logo

serve scaled logos and other pictures

implement user profile pages

implement dynamic questions (i.e. questions where numbers or other parts are generated on the fly on each view)

add in some AJAX

support more import formats

allow choice of site themes (e.g. green, blue, something else?)

comment stylesheet

use variables (and maybe even some eval evil) in stylesheet source to lessen the problem of copy/paste 

maybe split stylesheet into multiple files

attempt to detect if browser doesn't support image maps

^ =~ /([^\n]*)\n/g
) . "</ul>");

print MindMeld->footer;

