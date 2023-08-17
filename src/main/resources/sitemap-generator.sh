#!/bin/bash
sitedomain="$1"
dir="$2"
wget --user-agent='Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36' \
 --referer='https://www.google.com/' \
 --execute robots=off \
 --no-check-certificate \
 --recursive \
 --level=inf \
 --quota='5000m' \
 --spider \
 --no-directories \
 --no-verbose \
 --accept '*.htm,*.html,*.php,*.jsp,*.pdf,*.pptmhtml,*.php3,*.ppthtml,*.ognc,*.phtm,*.jhtml,*.do,*.jsp,*.jspx,*.dochtml,*.docmhtml,*.mhtml,*.phtml,*.rhtml,*.chm,*.ap,*.asp,*.php2,*.php4,*.gne,*.dhtml,*.vrt,*.jspa,*.a5w,*.ssp,*.xhtm' \
 --output-file=$dir/linklist.txt $sitedomain
grep -i '\[1\]$' $dir/linklist.txt | awk -F 'URL:' '{print $2}' | awk '{$1=$1};1' | awk '{print $1}' | sort -u | sed '/^$/d' > $dir/sortedurls.txt
header='<?xml version="1.0" encoding="UTF-8"?><urlset
      xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9
            http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">'
echo $header > sitemap.xml
while read p; do
  case "$p" in
  */ | *.html | *.htm | *.php | *.jsp | *.pdf | *.pptmhtml | *.php3 | *.ppthtml | *.ognc | *.phtm | *.jhtml | *.do | *.jsp | *.jspx | *.dochtml | *.docmhtml | *.mhtml | *.phtml | *.rhtml | *.chm | *.ap | *.asp | *.php2 | *.php4 | *.gne | *.dhtml | *.vrt | *.jspa | *.a5w | *.ssp | *.xhtm)
    echo '<url><loc>'$p'</loc></url>' >> sitemap.xml
    ;;
  *)
    ;;
 esac
done < $dir/sortedurls.txt
echo "</urlset>" >> sitemap.xml
