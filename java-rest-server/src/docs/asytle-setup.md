* The purpose of astyle is to enforce consistent coding style across all developers in this component.
* Visit webpage for details: http://astyle.sourceforge.net/install.html
* For mac, install asytle using:

           brew install astyle

* Copy the `pre-commit` file into your `.git/hooks/` directory
* That's it. Happy coding.


* You can also use the following line to format any file you wish with the same style we have:

```
           asytle \
                --style=allman \
                --add-brackets \
                --lineend=linux \
                --max-code-length=120 \
                --break-after-logical \
                --break-blocks \
                --pad-oper \
                --pad-paren-in \
                --close-templates \
                --delete-empty-lines <file_path>
```
