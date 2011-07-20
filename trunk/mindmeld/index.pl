#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

print MindMeld->header;

print q`<p><strong>MindMeld</strong> will be a system for training your mind like a Vulcan. Not becoming an emotionless automaton is left as an exercise for the user.</p>
<p><iframe width="853" height="510" src="https://www.youtube.com/embed/ds7dBoWLlrc?rel=0" frameborder="0" allowfullscreen></iframe></p>`;

print MindMeld->footer;

