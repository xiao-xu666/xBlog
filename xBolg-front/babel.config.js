module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset'
  ],
  plugins: [
    [
      'prismjs',
      {
        languages: [
          'html',
          'css',
          'javascript',
          'php',
          'dart',
          'bash',
          'nginx',
          'sql',
          'c',
          'cpp',
          'python',
          'go',
          'java',
          'bash',
          'json'
        ],
        plugins: [
          'line-numbers',
          'show-language',
          'copy-to-clipboard'
        ],
        // theme: 'tomorrow' // 主题颜色
      }
    ]
  ]
}
