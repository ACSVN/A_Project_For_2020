# A_Project_For_2020

<br />
<br />

## 使用準備 (Preparation)

### 1. input.xlsx内 `Console` シートでwebsiteシートとkeywordシートの読み取り開始行と読み取り終了行を指定する  
#### (By editting `console` sheet in input.xlsx, set start and finish row to read "website" & "keyword" sheet)  
- 全てのウェブクローリングの場合、`website: 2~25行目`、`keyword: 2~19行目`と指定してください  
- (If you want to search all website and keywords, set `website: Row No.2~25`, `keyword: Row No.2~19`)

<br />

### 2. input.xlsx内 `website` シートで各サイトについてウェブクローリングを行うか、ページデータの保存を行うかを指定する  
#### (By editting `website` sheet in input.xlsx, set whether or not will do web-crawling or saving page for each website)  
- [CrawlSwitch] 1: 実行する(Run) / 0: 実行しない(Not Run)
- [SaveSwitch] 1: 実行する(Run) / 0: 実行しない(Not Run)

<br />

### 3. input.xlsx内 `website` シートで各サイトのウェブクローリングの設定を確認する
#### (Confirm setting of the web-crawling for each website)  
- [CrawlMethod] 1: ANA1 / 2: ANA2
- [SaveMethod] 1: HTTrack / 2: Google Chrome (Ctrl+S)
- [PageNum] ウェブクローリングの必要ページ数 (Necessary page number for web-crawling)
- [LoadWaitTime] ウェブサイト待機時間 (Waiting time for opening webpage)
- [SaveWaitTime] ページ保存待機時間 (Waiting time for saveing webpage)

<br />

### 4. input.xlsx内 `keyword` シートで検索するキーワードリストを確認する
#### (Confirm keyword list to search on each website)

<br />
<br />

## 使用方法 (How to Use)

### 1. HeartCoreRoboを起動し、このプロジェクトを選択する  
#### (Open HeartCoreRobo and select this project)

<br />

### 2. `main.tpr` を開き、`F5` ボタンを押す
#### (Open `main.tpr` and push `F5` button)

<br />
<br />

## HeartCoreRoboでの便利なコマンド

- スクリプト編集時にコメントアウトしたい部分がある場合、該当範囲を選択して `Ctrl+/` を押す
- 実行するときはマウスを画面から離して `F5` ボタンを押すとマウスコントロールを奪われず自動で操作される
