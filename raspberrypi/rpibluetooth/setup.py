try:
    from setuptools import setup
except ImportError:
    from distutils.core import setup

config = {
    'description': 'ECE Senior Design Spring 2017',
    'author': 'Sharukh Hasan, Amanda Beadle, Heba Omar, Rui Xi, Richard',
    'url': 'https://github.com/sharukhhasan/ECESeniorDesign',
    'download_url': 'https://github.com/sharukhhasan/ECESeniorDesign.git',
    'author_email': 'sharukh-hasan@uiowa.edu',
    'version': '0.1',
    'install_requires': ['nose'],
    'packages': ['rpi'],
    'scripts': [],
    'name': 'rpibluetooth'
}

setup(**config)