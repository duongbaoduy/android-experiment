#!/usr/bin/env python 
from cStringIO import StringIO
import os
import sys
import tarfile
import urllib
import zipfile
import subprocess


def name_from_url(url):
    basename = os.path.basename(url)
    if (basename.find('tar') > 0):
        return os.path.splitext(os.path.splitext(basename)[0])[0]
    return os.path.splitext(basename)[0]

def extract_file(url, dir_path='.'):
    lfile = None
    if url.endswith('.zip'):
        opener, mode = zipfile.ZipFile, 'r'
        lfile = StringIO(urllib.urlopen(url).read())
    elif url.endswith('.tar.gz') or url.endswith('.tgz'):
        opener, mode = tarfile.open, 'r:gz'
    elif url.endswith('.tar.bz2') or url.endswith('.tbz'):
        opener, mode = tarfile.open, 'r:bz2'
    else: 
        raise ValueError, "Could not extract `%s` as no appropriate extractor is found" % url
    
    cwd = os.getcwd()
    os.chdir(os.path.dirname(dir_path))

    try:
        if lfile is None:
            file = opener(fileobj=StringIO(urllib.urlopen(url).read()), mode=mode)
        else:
            file = opener(lfile, mode)
        try: file.extractall(dir_path)
        finally: file.close()
    finally:
        os.chdir(cwd)

def install_lib():
    # get package
    get_list = [
                ]
    libs_dir = 'lib'
    for url, name in get_list:
        if name:
            contain_dir = os.path.join(curr_dir, libs_dir, name)
        else:
            contain_dir = os.path.join(curr_dir, libs_dir, name_from_url(url))
        if (os.path.exists(contain_dir)):
            print contain_dir, 'installed'
        else:
            print 'installing ...  ' , url
            extract_file(url, contain_dir)
            
            
    print 'libs install done !!'

def install_py_lib():
    print 'install external library ...' 
    os.system(pip_bin + ' install -U pip')
    pkg_libs = []
    for item in pkg_libs:
        pip_cmd = 'install'
        pip_arg = ''
        pip_param = item
        os.system(pip_bin + '  ' + pip_cmd + '  ' + pip_arg + '  ' + pip_param)
    print 'Done' 

def get_git():
    os.chdir('lib')
    os.system('[ -d androidplot  ] || git clone https://bitbucket.org/androidplot/androidplot.git androidplot')

def install():
    os.system('[ -d lib ] || mkdir lib')
    install_lib()
    install_py_lib()
    get_git()

curr_dir = os.path.dirname(__file__)
python_bin = sys.executable;
pip_bin = os.path.dirname(python_bin) + '\Scripts\pip.exe'
os.environ['VS90COMNTOOLS'] = os.environ['VS120COMNTOOLS'] 
os.environ['CXX'] = 'g++'
os.environ['CC'] = 'gcc'

if __name__ == '__main__':
    install()
