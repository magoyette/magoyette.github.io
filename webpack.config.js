const path = require("path");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const StylelintPlugin = require("stylelint-webpack-plugin");

module.exports = {
  mode: "production",
  // Two entries are necessary because the styles aren't linked to the JavaScript files
  entry: ["./sass/styles.scss"],
  output: {
    path: path.resolve(__dirname, "./static/"),
  },
  module: {
    rules: [
      {
        test: /\.(sa|sc|c)ss$/,
        include: [path.resolve(__dirname, "sass")],
        use: [
          {
            loader: MiniCssExtractPlugin.loader,
            options: {
              publicPath: "",
            },
          },
          "css-loader",
          "postcss-loader",
          "sass-loader",
        ],
      },
      {
        test: /\.(woff(2)?|ttf|eot|svg)(\?v=\d+\.\d+\.\d+)?$/,
        use: [
          {
            loader: "file-loader",
            options: {
              name: "[name].[ext]",
              outputPath: "fonts/",
            },
          },
        ],
      },
    ],
  },
  // Configure mini-css-extract-plugin to extract the CSS in a single file
  optimization: {
    splitChunks: {
      cacheGroups: {
        styles: {
          name: "styles",
          test: /\.css$/,
          chunks: "all",
          enforce: true,
        },
      },
    },
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: "styles.css",
    }),
    new StylelintPlugin({
      context: "sass",
      // Set to true to prevent build from failing on errors
      emitWarning: false,
    }),
  ],
};
