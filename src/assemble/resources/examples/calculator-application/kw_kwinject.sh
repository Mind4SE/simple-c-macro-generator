#!/bin/bash
#KLOCWORK_LICENSE_FILE=27002@PFRWFLM2.eur.gad.schneider-electric.com;27002@PFRWFLM3.eur.gad.schneider-electric.com;27002@W7FRH1430D.eur.gad.schneider-electric.com

KW_HOST=http://QFRWSCA1.eur.gad.schneider-electric.com:8082
KW_USER=ECP_SF_Admin
BUILDSPEC_FILE=buildspec_kwinject.out
KW_PROJECT=MIND-static-analysis-doc
KW_TABLES=my_tables_kwinject

rm -rf $KW_TABLES

kwinject --output $BUILDSPEC_FILE make analysis

kwbuildproject --force --url http://QFRWSCA1.eur.gad.schneider-electric.com:8082/$KW_PROJECT --tables-directory $KW_TABLES $BUILDSPEC_FILE

kwadmin --url $KW_HOST load $KW_PROJECT $KW_TABLES

