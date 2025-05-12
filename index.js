// index.js
require('dotenv').config();
const express = require('express');
const axios = require('axios');
const app = express();
app.use(express.json());

// 환경 변수에서 민감 정보 읽기
const GITHUB_TOKEN = process.env.GITHUB_TOKEN;
const GITHUB_OWNER = process.env.GITHUB_OWNER;
const GITHUB_REPO = process.env.GITHUB_REPO;

app.post('/jira-webhook', async (req, res) => {
  console.log('📩 Webhook 요청 도착:', JSON.stringify(req.body));
  try {
    const issue = req.body.issue;
    const key = issue.key;
    const summary = issue.fields.summary;
    const description = issue.fields.description || 'No description.';

    const response = await axios.post(
        `https://api.github.com/repos/${GITHUB_OWNER}/${GITHUB_REPO}/issues`,
        {
          title: `[Jira ${key}] ${summary}`,
          body: description
        },
        {
          headers: {
            Authorization: `Bearer ${GITHUB_TOKEN}`,
            'Accept': 'application/vnd.github+json',
          }
        }
    );

    console.log('✅ GitHub 이슈 생성 성공:', response.data.html_url);
    res.status(200).send('GitHub issue created!');
  } catch (error) {
    console.error('❌ GitHub 이슈 생성 실패:', error.response?.data || error.message);
    res.status(500).send('GitHub issue creation failed');
  }
});

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`🚀 Server listening on http://localhost:${PORT}`);
});
