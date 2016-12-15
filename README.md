# (Etude) NewSunshine 
# App Android pour la prévision météo 
##
## L'utilisation 
##
## La liste des villes avec le météo du jour à l'ouverture de l'app
##

![screenshot_1478786760](https://cloud.githubusercontent.com/assets/21304543/20180235/f66c5e78-a759-11e6-8567-301d8885273a.png)

##
## Le météo pour 7 jours par ville pour la ville cliquée
##

![screenshot_1478786791](https://cloud.githubusercontent.com/assets/21304543/20180236/f675c6f2-a759-11e6-97fe-fddea8034847.png)

##
## L'information détaillée par jour/ville. Le menu "MAP" affiche la carte de la ville en utilisant une app tiers.
##

![screenshot_1478786823](https://cloud.githubusercontent.com/assets/21304543/20180237/f677279a-a759-11e6-93dd-dd9aa68eff03.png)

##
## Menu principal pour le paramétrage 
##
![screenshot_1478787010](https://cloud.githubusercontent.com/assets/21304543/20180241/f68cfc32-a759-11e6-813b-612b49a0ce67.png)

##
##
## Editer la liste des villes (l'ajout et la suppression)
##

![screenshot_1478786853](https://cloud.githubusercontent.com/assets/21304543/20180238/f67cf116-a759-11e6-8549-85e115bf9490.png)


## Choix de langue via application (défault est celui de l'appareil téléphonique)
##
![screenshot_1478786895](https://cloud.githubusercontent.com/assets/21304543/20180239/f6812ee8-a759-11e6-8abe-13d20313ca3f.png)
##

## Information
##
La prévision météo est récpérée à partir de http://api.openweathermap.org
La synchoronisation entre le site et l'application se fait 
- à l'ouverture de l'app 
- lors de l'ajout de nouvelles villes 
- toutes les 3 heures (paramétrable)
## 


## Prerequis
##
SDK API 19 ou supérieur
##
## Techniques utilisées
- ContentProvider et SQLite
- SynchAdapter
- ExpandableList, CursorAdapter, CursorLoader
- Intent Implicite et Explicite
- JSon (Gson)
- Fragments


