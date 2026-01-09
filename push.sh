git push origin --all
git push gitee --all
git push github --all

#删除部分分支
#git branch -d -r origin/dev
#git branch -d -r gitee/dev
#git branch -d -r github/dev

# 扮装所有的分支
git push origin --tags
git push gitee --tags
git push github --tags

#mvn clean package -Pdev -DskipTests