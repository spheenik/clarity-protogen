#!/bin/sh
mkdir SteamKit
cd SteamKit
git init
git config core.sparsecheckout true
echo Resources/Protobufs/dota >> .git/info/sparse-checkout
git remote add -f origin https://github.com/SteamRE/SteamKit.git
git pull origin master